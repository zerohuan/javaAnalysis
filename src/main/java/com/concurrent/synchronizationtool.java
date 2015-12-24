package com.concurrent;

import java.util.concurrent.CountDownLatch;

/**
 * Created by yjh on 15-12-22.
 */
public final class synchronizationtool {


    /**
     * 测试闭锁，启动nThreads个线程，等待“启动门”同时开始执行
     * 所有任务线程执行完成后关闭结束门
     * @param nThreads
     * @param task
     */
    private static void testLatch(int nThreads, Runnable task)
            throws InterruptedException {
        long startTime = System.nanoTime();
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(nThreads);
        for (int i = 0; i < nThreads; ++i) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        startLatch.await(); //等待“启动门”开启
                        try {
                            task.run();
                        } finally {
                            endLatch.countDown();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
