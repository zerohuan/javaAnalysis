package com.concurrent.executors.webServerByExecutor;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 带有时间统计功能的服务器模型
 * 基于BIO，Socket处理请求；
 *
 * Created by yjh on 16-1-9.
 */
public final class WebServer extends ThreadPoolExecutor {
    /*
    时间统计支持
     */
    private final ThreadLocal<Long> startTime = new ThreadLocal<>();
    private final AtomicLong numTasks = new AtomicLong(); //任务数量
    private final AtomicLong totalTime = new AtomicLong(); //总执行时间
    private static final Logger logger = LogManager.getLogger("M_WebServer LOGGER");

    private AfterStarted afterStarted;

    private WebServer(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                     TimeUnit unit, BlockingQueue<Runnable> workQueue,
                     ThreadFactory threadFactory, RejectedExecutionHandler handler,
                      AfterStarted afterStarted) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        this.afterStarted = afterStarted;
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        try {
            long endTime = System.nanoTime();
            long taskTime = endTime - startTime.get();
            numTasks.incrementAndGet();
            totalTime.addAndGet(taskTime);
            logger.info(String.format("Thread %s, end %s, time %s", t, r, taskTime));
        } finally {
            super.afterExecute(r, t);
        }
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        logger.info(String.format("Thread %s : start %s", t, r));
        startTime.set(System.nanoTime());
    }

    @Override
    protected void terminated() {
        try {
            logger.info("M_WebServer Terminated. Num of task %d, totalTime %d", numTasks.get(), totalTime.get());
        } finally {
            super.terminated();
        }
    }

    /*
    关闭服务器，对以提交的请求进行处理
     */
    @Override
    public void shutdown() {
        try {
            started.set(false);
        } finally {
            super.shutdown();
        }
    }

    private AtomicBoolean started = new AtomicBoolean(false);
    private static final int PORT = 8081;
    public void start() {
        if (started.compareAndSet(false, true)) {
            logger.info("M_Server start");
            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                if (afterStarted != null)
                    afterStarted.afterStarted();
                //响应中断
                while (!Thread.currentThread().isInterrupted()) {
                    Socket socket = serverSocket.accept();
                    submit(new RequestHandler(socket));
                }
            } catch (IOException e) {
                logger.error(e);
            }
        }
    }

    private static class RequestHandler implements Callable<String> {
        private final Socket socket;

        public RequestHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public String call() throws Exception {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream())) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.equals("QUIT"))
                        break;
                    logger.info(String.format("Receive message: %s", line));
                }
                return "SUCCESS END";
            } finally {
                socket.close();
                logger.info(String.format("%s closed", this));
            }
        }
    }

    public interface AfterStarted {
        void afterStarted();
    }


    /**
     * 创建WebServer工厂方法，使用有界线程池，SynchronousQueue，
     * @return WebServer实例
     */
    public static WebServer newWebServer(AfterStarted afterStarted) {
        return new WebServer(30, 30, 60L,
                TimeUnit.SECONDS, new SynchronousQueue<>(),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        if (r == null)
                            throw new NullPointerException();
                        return new Thread(r);
                    }
                }, new ThreadPoolExecutor.CallerRunsPolicy(), afterStarted);
    }

    /*
    测试
    JVM参数：-server -Xmx100m -Xms100m -XX:NewRatio=4 -XX:+PrintGCDetails -XX:+UseCompressedOops -XX:+UseConcMarkSweepGC
     -XX:+UseParNewGC


     */
    private static WebServer server; //测试用
    public static void main(String[] args) throws Exception {
        CountDownLatch start = new CountDownLatch(1);
        //开启服务器线程
        new Thread(() -> {
            server = WebServer.newWebServer(new AfterStarted() {
                @Override
                public void afterStarted() {
                    start.countDown();
                }
            });
            server.start();
        }).start();
        //每隔1秒100个请求
        start.await();
        for (int j = 0; j < 100; ++j) {
            for (int i = 0; i < 100; ++i) {
                new Thread(() -> {
                    try (Socket socket = new Socket("localhost", 8081)) {
                        try (PrintWriter out = new PrintWriter(socket.getOutputStream())) {
                            out.println(Talker.randomSentence());
                            out.flush();
                            out.println("QUIT");
                            out.flush();
                        }
                    } catch (IOException e) {
                        logger.error(String.format("Thread %s, error %s", Thread.currentThread(), e));
                    }
                }).start();
            }
            TimeUnit.SECONDS.sleep(1);
        }
    }

    /*
    随机输出一段请求数据
     */
    private static class Talker {
        private static final File CONTENT_TXT;
        private static final String FILENAME = "/home/yjh/dms/tmp/说明.txt";
        private static final String CONTENT;
        private static final int CONTENT_LENGTH;
        private static final Random rnd = new Random(); //Random是线程安全的

        /*
        初始化谈话机
         */
        static {
            CONTENT_TXT = new File(FILENAME);
            if (!CONTENT_TXT.exists())
                throw new IllegalStateException("文件不存在");
            try {
                CONTENT = IOUtils.toString(new FileInputStream(CONTENT_TXT));
                CONTENT_LENGTH = CONTENT.length();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public static String randomSentence() {
            int start;
            return CONTENT.substring(start = rnd.nextInt(CONTENT_LENGTH - 10), start + rnd.nextInt(CONTENT_LENGTH -
                    start));
        }

        public static void main(String[] args) {
            System.out.println(Talker.randomSentence());
        }
    }
}
