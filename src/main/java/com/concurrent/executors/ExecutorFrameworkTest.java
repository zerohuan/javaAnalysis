package com.concurrent.executors;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

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
    private static void timerTest() {
        Timer timer = new Timer();
        TimerTask t100 = new TimerTask() {
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
        TimerTask t400 = new TimerTask() {
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
        timer.schedule(t100, 0, 1000);
        timer.schedule(t400, 0, 1000);
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

    public static void main(String[] args) throws InterruptedException  {
//        executorStatus();
        timerTest();

    }
}
