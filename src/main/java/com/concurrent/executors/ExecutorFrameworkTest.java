package com.concurrent.executors;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

/**
 *
 * Created by yjh on 15-12-26.
 */
public class ExecutorFrameworkTest {
    //一个简单的线程池，阻塞IO的服务器
    private static class SimpleThreadPoolWebServer {
        private static final int THREAD_COUNT = 100;
        private static final ExecutorService executorService =
                Executors.newFixedThreadPool(THREAD_COUNT);

        public static void start() throws IOException {
            try (ServerSocket serverSocket = new ServerSocket(8080)) {
                while (!executorService.isShutdown()) {
                    //这个阻塞不可被中断，因此这里主线程设置“停止”后，一次请求处理后才能“停止”
                    Socket socket = serverSocket.accept();
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            handleRequest(socket);
                        }
                    });
                }
            }
        }

        public static void handleRequest(Socket socket) {
            if (isShutDownRequest(socket)) {
                executorService.shutdown(); //拒绝新的请求
            } else {
                dispatchRequest(socket);
            }
        }

        public static boolean isShutDownRequest(Socket socket) {
            //TODO 增加判断是否为关闭请求
            return false;
        }

        public static void dispatchRequest(Socket socket) {
            //TODO 分派到指定处理代码进行处理
        }
    }

    /*
    Executor的运行，关闭，已终止3种状态
     */
    private static class TimeTask implements Runnable {
        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private static void executorStatus() throws InterruptedException {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                ExecutorService executor = Executors.newFixedThreadPool(10);
                for (int i = 0; i < 10; ++i) {
                    executor.execute(new TimeTask());
                }
                //t：RUNNABLE
                try {
                    //该方法有3种结果，1 在指定时间内终止；2 超时；3 被中断；
                    executor.awaitTermination(4, TimeUnit.SECONDS); //TIMED_WAITING
                    //RUNNABLE
                    printStatus(executor);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //新任务将不再被添加，此时isShutDown为true，isTerminated可能为false
                executor.shutdown();
                try {
                    executor.execute(new TimeTask()); //将会抛出RejectedExecutionException
                } catch (RejectedExecutionException e) {
                    printStatus(executor);
                }
                printStatus(executor);

            }
        });
        t.start();
        TimeUnit.SECONDS.sleep(1); //TIMED_WAITING
        printStatus(t);
    }

    /*
    周期任务
     */
    /*
    Timer的缺陷：1 定时精确性（Timer只有一个工作线程，任务执行时刻将会受到其他执行时间长短的影响）；2 抛出异常可能导致“线程泄漏”；
    我简单看了下源码Timer使用一个基于数组的最小堆实现的任务队列（基于下一次将执行的时间比较大小）
    当队列满的时候，数组扩容为原来的2倍；
    现在应当尽量避免使用Timer
     */
    private static final TimerTask t100 = new TimerTask() {
        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(1);
                System.out.println("Timer 100");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
    private static final TimerTask t400 = new TimerTask() {
        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(4);
                System.out.println("Timer 400");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
    private static void timerTest() {
        Timer timer = new Timer();

        timer.schedule(t100, 0, 1000);
        timer.schedule(t400, 0, 1000);
        timer.scheduleAtFixedRate(t100, 0, 1000);
        timer.scheduleAtFixedRate(t400, 0, 1000);
        t100.cancel();
        t400.cancel();
        //未检查的异常将导致工作线程停止，直接over
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                throw new IllegalStateException();
            }
        }, 100, 100);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("t!");
            }
        }, 0, 1000);

    }
    /*
    scheduledThreadPool，同样也是基于堆的优先队列实现;
    ScheduledThreadPoolExecutor将任务包装成Comparable的延时/周期任务
    (RunnableScheduledFuture装饰器模式)实现了Delay，根据下一次执行的时间进行比较
     */
    private static void testScheduledThreadPool() {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);
//        executorService.scheduleAtFixedRate(t100, 0, 1, TimeUnit.SECONDS);
//        executorService.scheduleAtFixedRate(t400, 0, 4, TimeUnit.SECONDS);
        executorService.schedule(t100, 1, TimeUnit.SECONDS);
    }


    /*
    Executor基本用法
     */
    //Future 可以cancel，get：
    private static Callable<String> timeTask = new Callable<String>() {
        @Override
        public String call() throws Exception {
            TimeUnit.SECONDS.sleep(2);
            return "success";
        }
    };
    private static void testFutureResult() {
        ExecutorService service = Executors.newFixedThreadPool(10);
        Future<String> f = service.submit(timeTask);
        try {
            //当前线程状态变化：RUNNABLE—>TIMED_WAITING->RUNNABLE
            f.cancel(true);
            f.get(1, TimeUnit.SECONDS);
        } catch (CancellationException e) {
            System.out.println("CancellationException");
        } catch (ExecutionException e) {
            /*
            可以通过ExecutionException.getCause获取任务中发生的异常
             */
            System.out.println("ExecutionException");
        } catch (TimeoutException e) {
            /*
            设置了超时时间
             */
            System.out.println("TimeoutException");
        } catch (InterruptedException e) {
            /*
            被中断
             */
            System.out.println("InterruptedException");
        }
        service.shutdown(); //拒绝新的任务
//        service.shutdownNow(); //停止正在执行的任务进程（线程），本质上是通过中断
    }

    private static final class CancelFalseTest {
        private static final ExecutorService executors = Executors.newFixedThreadPool(2);

        public static void main(String[] args) throws InterruptedException {
            class SleepTask implements Callable {
                @Override
                public Object call() throws Exception {
                    try {
                        TimeUnit.SECONDS.sleep(2);
                        System.out.println("wake up");
                    } catch (InterruptedException e) {
                        System.out.println("interrupted");
                    }
                    return null;
                }
            }
            Future<?> f = executors.submit(new SleepTask());
            TimeUnit.SECONDS.sleep(1);
            f.cancel(false);
//            try {
//                f.get();
//            } catch (ExecutionException e) {
//                System.out.println(e.getCause());
//            } catch (Exception e) {
//                System.out.println(e);
//            }
            executors.shutdown();
        }
    }

    /*
    CompletionService：Future
    基于组合，提供一个阻塞队列来解耦客户端（消费者）和生产者
    每个执行任务完成后向阻塞队列中添加结果；
    默认使用LinkedBlocked，可以重新设置；
    poll有非阻塞和计时等待两个版本
    take是阻塞获取
     */
    private static final class CompletionTask implements Callable<String> {
        @Override
        public String call() throws Exception {
            TimeUnit.SECONDS.sleep(1);
            return "Success";
        }
    }
    private static void completionServiceTest() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CompletionService<String> service = new ExecutorCompletionService<String>(executorService);
        int num = 10;
        for (int i = 0; i < num; ++i) {
            service.submit(new CompletionTask());
        }
        System.out.println(service.poll() == null);
        //先执行完的任务，先输出结果
        for (int i = 0; i < num; ++i) {
            System.out.println(service.take().get());
        }
        executorService.shutdown();
    }


    private static void printStatus(Object o) {
        if (o instanceof Thread) {
            Thread t = (Thread)o;
            System.out.println("Thread " + t.getName() + " " + t.getState());
        }
        else if (o instanceof ExecutorService) {
            ExecutorService e = (ExecutorService)o;
            System.out.println("Executor " + e.isShutdown() + " " + e.isTerminated());
        }
    }
    static class SleepTask implements Runnable {
        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().isInterrupted());
                TimeUnit.SECONDS.sleep(2);
                System.out.println("wake up");
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            }
        }
    }
    private static void shutdownTest() throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        executorService.execute(new SleepTask());
        executorService.execute(new SleepTask());
        TimeUnit.SECONDS.sleep(1);
        executorService.shutdownNow();
    }

    private static class AwaitTerminationTest {
        private static void awaitTerminationTest(long timeout, TimeUnit unit)
                throws InterruptedException {
            ExecutorService executorService = Executors.newFixedThreadPool(1);
            try {
                executorService.execute(new SleepTask());
                executorService.execute(new SleepTask());
            } finally {
                executorService.shutdown();
                executorService.awaitTermination(timeout, unit); //超时所在线程从等待中解除，任务还在执行
                System.out.println("return from await");
            }
        }

        public static void main(String[] args) throws InterruptedException {
            awaitTerminationTest(1, TimeUnit.SECONDS);
        }
    }

    public static void main(String[] args) throws Exception  {
//        executorStatus();
//        timerTest();
//        testScheduledThreadPool();
//        testFutureResult();
//        completionServiceTest();
        shutdownTest();
    }

}
