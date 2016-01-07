package com.concurrent.util;

import java.util.concurrent.TimeUnit;

/**
 * Created by yjh on 16-1-7.
 */
public class SleepTask implements Runnable {
    private final long timeout;
    private final TimeUnit unit;

    public SleepTask(long timeout, TimeUnit unit) {
        this.timeout = timeout;
        this.unit = unit;
    }

    protected void afterSleep() {

    }

    @Override
    public void run() {
        try {
            unit.sleep(timeout);
            afterSleep();
        } catch (InterruptedException e) {
            System.out.println("interrupted");
            //恢复中断状态
            Thread.currentThread().interrupt();
        } finally {
            System.out.println("end");
        }
    }
}
