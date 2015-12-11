package com.yjh.concurrent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 测试JDK中提供的不同线程池实现
 * Created by yjh on 15-11-8.
 */
public class ThreadPoolTest {
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static ExecutorService cachedThreadPool =
            Executors.newCachedThreadPool();
    private static ExecutorService singleThreadPool =
            Executors.newSingleThreadExecutor();
    private static ExecutorService scheduledThreadPool =
            Executors.newScheduledThreadPool(CPU_COUNT + 1);
    private static ExecutorService CPUFixedThreadPool =
            Executors.newFixedThreadPool(CPU_COUNT + 1);
    private static ExecutorService IOFixedThreadPool =
            Executors.newFixedThreadPool(4 * CPU_COUNT + 1);
    private static ExecutorService monitorThreadPool =
            new MonitorThreadPool(2 * CPU_COUNT + 1);

    private static class MonitorThreadPool extends ThreadPoolExecutor {
        private static final Map<Runnable, Long> timeMap =
                new HashMap<>();
        private static final AtomicLong atomicLong = new AtomicLong();

        public MonitorThreadPool(int corePoolSize) {
            super(corePoolSize, corePoolSize, 60l, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
        }

        @Override
        protected void beforeExecute(Thread t, Runnable r) {
            super.beforeExecute(t, r);
            timeMap.put(r, System.currentTimeMillis());
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            super.afterExecute(r, t);
            long oldValue = atomicLong.get();
            long addValue = System.currentTimeMillis() - timeMap.get(r);
            if(atomicLong.compareAndSet(oldValue, oldValue + addValue)) {
                System.out.println("Average time is " + atomicLong.get() / (this.getCompletedTaskCount() + 1));
            }
        }

        @Override
        protected void terminated() {
            super.terminated();
        }
    }

    private static class CpuIntensiveTask implements Runnable {
        @Override
        public void run() {
            double d = 0.0;
            for(int i = 0; i < Integer.MAX_VALUE; i++) {
                d += 100 * Math.sqrt(i);
            }
        }
    }

    private static class IOIntensiveTask implements Runnable {
        @Override
        public void run() {
            File f = new File("/home/yjh/test.file");
            File f2 = new File("/home/yjh/test.file2");
            byte[] bytes = new byte[50 * 1024];
            if(f.exists()) {
                try(FileInputStream inputStream = new FileInputStream(f);
                    FileOutputStream outputStream = new FileOutputStream(f2)) {
                    int len;
                    while((len = inputStream.read(bytes)) != -1) {
                        TimeUnit.MILLISECONDS.sleep(1);
                        outputStream.write(bytes, 0, len);
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void MonitorTest() {
        for(int i = 0; i < 20; i++) {
            monitorThreadPool.execute(new CpuIntensiveTask());
        }
        monitorThreadPool.shutdown();

        System.out.println("monitorThreadPool " + monitorThreadPool.isShutdown() +
                " " + monitorThreadPool.isTerminated());
    }

    public static void CPUTest() {
        for(int i = 0; i < 20; i++) {
            CPUFixedThreadPool.execute(new CpuIntensiveTask());
        }
        CPUFixedThreadPool.shutdown();

        System.out.println("CPUFixedThreadPool " + CPUFixedThreadPool.isShutdown() +
                " " + CPUFixedThreadPool.isTerminated());
    }

    public static void IOTest() {
        for(int i = 0; i < 20; i++) {
            IOFixedThreadPool.execute(new IOIntensiveTask());
        }

        IOFixedThreadPool.shutdown();

        System.out.println("IOFixedThreadPool " + IOFixedThreadPool.isShutdown() +
                " " + IOFixedThreadPool.isTerminated());
    }

    public static void main(String[] args) {
        System.out.println("CPU数量：" + CPU_COUNT);
//        CPUTest();
//        IOTest();
        MonitorTest();
    }
}
