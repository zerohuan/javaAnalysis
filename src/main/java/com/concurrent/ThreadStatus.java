package com.concurrent;

import java.util.concurrent.TimeUnit;

/**
 * Created by yjh on 15-12-26.
 */
public class ThreadStatus {
    private static final Object mutex = new Object();

    private static void waitTime() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (mutex) {
                    try {
                        mutex.wait(10);
                        System.out.println("waited");
                    } catch (InterruptedException e) {
                        new RuntimeException(e);
                    }
                }
            }
        });
        t.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println(t.getName() + " " + e);
            }
        });
        t.start();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (mutex) {
                    try {
                        System.out.println(t + "" + t.getState());
                        TimeUnit.SECONDS.sleep(1);
                        System.out.println(t + "" + t.getState());
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t1.start();
        System.out.println(t1 + "" + t1.getState());
        System.out.println(t + "" + t.getState());
    }


    public static void main(String[] args) {
        waitTime();
    }
}
