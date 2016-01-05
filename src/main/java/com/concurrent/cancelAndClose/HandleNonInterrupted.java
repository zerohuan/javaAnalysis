package com.concurrent.cancelAndClose;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 处理不可中断的阻塞
 * （1）获取某个内置锁产生的阻塞，长时间阻塞很糟糕，没有什么好办法；
 * （2）java.io中的同步io，read方法阻塞并且不可中断；
 *  比如，ServerSocket的accept()方法，如果已经阻塞是不会响应close方法的，只有下一次调用时才抛出异常；
 *
 *
 * 可以通过close等方法中断的阻塞：
 * （1）nio中基于InterruptibleChannel实现的通道读写：
 *  interrupt()方法->抛出ClosedByInterruptException；
 *  close()方法->抛出AsynchronousCloseException；
 * （2）Selector的异步io； close()或者wakeup()，抛出ClosedSelectorException；
 * （3）java.io中的Socket的inputStream/outputStream的read；
 *
 * 对于io可以使用close方法来覆盖Thread的interrupt；
 *
 * 找出线程阻塞的原因，采取不同的“中断”方法，比如Socket的close方法
 *
 * Created by yjh on 16-1-4.
 */
public class HandleNonInterrupted {
    private static void blockedBySynchronized() {

    }

    /**
     * 对同步阻塞io的处理
     */
    /*
    Socket，方法一，自定义Thread
     */
    private static class ReaderThread extends Thread {
        private final Socket socket;
        private final InputStream in;

        public ReaderThread(Socket socket) throws IOException {
            this.socket = socket;
            in = socket.getInputStream();
        }

        @Override
        public void interrupt() {
            try {
                socket.close();
            } catch (IOException e) {
                /*忽略这个异常*/
            } finally {
                super.interrupt();
            }
        }

        @Override
        public void run() {
            try {
                //创建缓冲区
                byte[] buffer = new byte[1024];
                while (true) {
                    int i = in.read(buffer);
                    if (i < 0)
                        break;
                    else
                        processBuffer(buffer, 0, i);
                }
            } catch (IOException e) {
                //exit
            }
        }

        private void processBuffer(byte[] buffer, int start, int end) {

        }
    }

    /*
    Socket，用自定义Task，newTaskFor
     */
    private interface CancellableTask<T> extends Callable<T> {
        void cancel();
        RunnableFuture<T> newTask();
    }

    private class CancellingExecutor extends ThreadPoolExecutor {
        public CancellingExecutor(int corePoolSize, int maximumPoolSize,
                                  long keepAliveTime, TimeUnit unit,
                                  BlockingQueue<Runnable> workQueue) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        }

        @SuppressWarnings("unchecked")
        @Override
        protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
            if (callable instanceof CancellableTask) {
                return ((CancellableTask) callable).newTask();
            }
            else {
                return super.newTaskFor(callable);
            }
        }
    }

    private abstract class SocketingUsing<T> implements CancellableTask<T> {
        //由内置锁保证线程安全
        private Socket socket;

        protected synchronized void setSocket(Socket socket) {
            this.socket = socket;
        }

        @Override
        public synchronized void cancel() {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {/*忽略*/}
        }

        @Override
        public RunnableFuture<T> newTask() {
            return new FutureTask<T>(this) {
                @Override
                public boolean cancel(boolean mayInterruptIfRunning) {
                    try {
                        SocketingUsing.this.cancel();
                    } finally {
                        return super.cancel(mayInterruptIfRunning);
                    }
                }
            };
        }
    }

    /*
    ServerSocket中accept方法的阻塞
     */
    private static void testSocket() {
        class CT extends Thread {
            private ServerSocket serverSocket;
            @Override
            public void run() {
                synchronized (this) {
                    try {
                        serverSocket = new ServerSocket(8081);
                        //不可中断的阻塞，阻塞中，它也不会响应close并抛出异常
                        //只有下次调用accept时才可以检查是否已经关闭
                        Socket socket = serverSocket.accept();
                        /*...*/
                        System.out.println("broken");
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                }
            }

            public synchronized void close() {
                if (serverSocket != null) {
                    try {
                        serverSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        CT t;
        (t = new CT()).start();
        t.interrupt(); //没用
        t.close(); //没用
        System.out.println("Come here! " + t.getState());
    }

    /*
    InputStream和OutputStream中的阻塞
     */
    private static void testBIO() {
        class ST extends Thread {
            private final Socket socket;
            private final Scanner in;

            public ST(Socket socket) throws IOException {
                this.socket = socket;
                in = new Scanner(this.socket.getInputStream());
            }

            @Override
            public void run() {
                try {
                    if (socket != null) {
                        /*
                        阻塞，等待对方发送数据
                         */
                        while (in.hasNext()) { //hasNext是阻塞的
                            System.out.println(in.next());
                        }
                        //输出一下异常信息
                        Exception e;
                        if ((e = in.ioException()) != null) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace(System.out);
                }
            }

            public void close() throws IOException {
                if (socket != null) {
                    socket.close();
                    System.out.println("Socket close");
                }
            }
        }
        BlockingQueue<ST> queue = new ArrayBlockingQueue<>(10);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //一个服务器线程
                try (ServerSocket server = new ServerSocket(8081)) {
                    while (true) {
                        try {
                            ST t = new ST(server.accept());
                            t.start();
                            queue.put(t);
                        } catch (IOException | InterruptedException e) {
                            //打印一下，继续
                            System.out.println("has a exception" + e);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //loop，每开启一个socket连接，在这一进行测试
        while (true) {
            try {
                ST t = queue.take();
                //10秒后关闭
                TimeUnit.SECONDS.sleep(10);
                System.out.println("closed");
                t.close(); //手动关闭
                TimeUnit.SECONDS.sleep(1);
                System.out.println("t state: " + t.getState());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private static class TestClient {
        public static void main(String[] args) throws IOException {
            Socket socket = new Socket("localhost", 8081);
            //从键盘输入
            try (Scanner in = new Scanner(System.in);
                 PrintWriter out = new PrintWriter(socket.getOutputStream())) {
                while (in.hasNext()) {
                    String s = in.next();
                    //发送消息
                    out.println(s);
                    //PrintWriter的缓冲区默认是8192个字符，手动刷新，或者在构造器和中添加autoflush为true
                    out.flush();
                }
            }
        }
    }

    /*
    BIO的read阻塞，不可中断
     */
    private static class TestBioRead {
        public static void main(String[] args) throws Exception {
            InputStream in = System.in;
            Thread t;
            (t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("waiting read");
                        in.read();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            })).start();
            TimeUnit.SECONDS.sleep(1);
            t.interrupt();
            TimeUnit.SECONDS.sleep(1);
            System.out.println(t.getState()); //RUNNABLE
        }
    }

    /*
    Lock
     */
    private static final Lock lock = new ReentrantLock();
    private static void testLock() {
        lock.lock();
        try {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        lock.lockInterruptibly();
                        try {
                            System.out.println(Thread.currentThread() + " locked");
                        } finally {
                            lock.unlock();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
            TimeUnit.SECONDS.sleep(1);
            //将会输出WAITING而不是BLOCKED
            System.out.println(Thread.currentThread() + " state:" + t.getState());
            t.interrupt();
        } catch (Exception e) {} finally {
            lock.unlock();
        }

    }

    public static void main(String[] args) {
        testLock();
    }
}
