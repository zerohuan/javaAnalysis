package com.concurrent.executors;

import com.concurrent.util.SleepTask;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by yjh on 16-1-7.
 */
public class TrackingExecutor extends AbstractExecutorService {
    private final ExecutorService executors;
    private final Set<Runnable> taskCancelledSet = Collections.synchronizedSet(new HashSet<>());

    public TrackingExecutor(ExecutorService executors) {
        this.executors = executors;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return executors.awaitTermination(timeout, unit);
    }

    @Override
    public void shutdown() {
        executors.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return executors.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return executors.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return executors.isTerminated();
    }

    @Override
    public void execute(final Runnable command) {
        executors.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    command.run();
                } finally {
                    //线程池已关闭，当前线程被中断
                    if (isShutdown() && Thread.currentThread().isInterrupted()) {
                        taskCancelledSet.add(command);
                    }
                }
            }
        });
    }

    public Set<Runnable> getTaskCancelledSet() {
        return taskCancelledSet;
    }

    public static void main(String[] args) throws InterruptedException {
        TrackingExecutor executor = new TrackingExecutor(Executors.newFixedThreadPool(10));
        executor.execute(new SleepTask(2, TimeUnit.SECONDS));
        executor.execute(new SleepTask(2, TimeUnit.SECONDS));
        executor.execute(new SleepTask(2, TimeUnit.SECONDS));
        TimeUnit.SECONDS.sleep(1);
        executor.shutdownNow();
        TimeUnit.SECONDS.sleep(1);
        System.out.println(executor.getTaskCancelledSet().size());
    }
}
