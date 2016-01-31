package com.concurrent;

import com.concurrent.util.SleepTask;

import java.util.concurrent.TimeUnit;

/**
 * Created by yjh on 16-1-7.
 */
public class JVMClosing {

    public static void main(String[] args) throws Exception {
        SleepTask task = new SleepTask(10, TimeUnit.SECONDS) {
            @Override
            protected void afterSleep() {
                System.out.println("wake up");
            }
        };
        new Thread(task).start();
        Thread daemonT = new Thread(new SleepTask(10, TimeUnit.SECONDS) {
            @Override
            protected void afterSleep() {
                System.out.println("daemon wake up");
            }
        });
        daemonT.setDaemon(true);
        daemonT.start();
        //如果没有关闭钩子，将不会等待线程结束
        Runtime.getRuntime().addShutdownHook(new Thread(task)); //将会延长JVM关闭时间，同时如果有其他线程（守护和非守护线程）都一起并发执行
        TimeUnit.SECONDS.sleep(5);
        System.exit(0);
    }
}
