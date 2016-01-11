package com.concurrent.executors;

import java.util.concurrent.*;

/**
 * Created by yjh on 16-1-9.
 */
public class GentleDecreasePolicy {
    //一个有界线程池，SynchronousQueue，调用者运行的饱和策略，实现高负载时，平缓的性能下降
    private static final ExecutorService executor = new ThreadPoolExecutor(30, 30, 60L,
            TimeUnit.SECONDS, new SynchronousQueue<>(), null, new ThreadPoolExecutor.CallerRunsPolicy());

    /**
     *
     */
    private static class MonitorThreadFactory implements ThreadFactory {
        @Override
        public Thread newThread(Runnable r) {
            return null;
        }
    }
}
