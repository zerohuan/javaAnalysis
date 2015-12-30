package com.concurrent.cancelAndClose;

import com.util.ThreadUtils;

import java.util.concurrent.*;

/**
 * Created by yjh on 15-12-28.
 */
public class FutureCancel {
    //不可取消任务如果响应中断
    private static final BlockingQueue<String> bq = new ArrayBlockingQueue<>(10);
    private static  class UnCancelableTask {
        private boolean interrupted = false;
        public String loopMsg() {
            try {
                //直到有一条消息才退出
                while (true) {
                    try {
                        return bq.take();
                    } catch (InterruptedException e) {
                        interrupted = true;
                    }
                }
            } finally {
                //捕获到中断异常，在退出时恢复
                if (interrupted)
                    Thread.currentThread().interrupt();
            }
        }
    }

    /*
    计时任务，为什么我们最好使用Future来控制
     */
    /*
    方法一：用ScheduleExecutorService来中断当当前线程
    问题：timedRun1函数并不知道当前线程的中断策略：
    （1）任务r并不一定能响应中断，超时限制对任务r没有限制；
    （2）任务r可能在超时限制前结束，那么cancelExt中的中断请求会在任务返回后才发出；
     */
    private static final ScheduledExecutorService cancelExt =
            Executors.newScheduledThreadPool(1);
    private static void timedRun1(Runnable r, long timeout, TimeUnit unit) {
        Thread thread = Thread.currentThread();
        cancelExt.schedule(new Runnable() {
            @Override
            public void run() {
                thread.interrupt();
            }
        }, timeout, unit);
        r.run();
    }
    /*
    方法二：使用join库函数，让任务在单独线程中运行，使用带计时参数的join让timedRun所在线程实现计时等待
    PS：join是通过基于Thread对象的内置锁上进行wait来实现的，当线程终止的时候会在该Thread对象的内置锁上进行一次
    notifyAll；
    问题：join（计数版本）返回返回时，有两种可能：一是超时；二是线程终止；因此这里无法区分是正常返回还是超时退出了；
     */
    private static void timedRun2(final Runnable r, long timeout, TimeUnit unit)
            throws InterruptedException {
        //通过一个局部内部类来包装执行任务
        class TimedTask implements Runnable {
            Throwable t;
            @Override
            public void run() {
                try {
                    r.run();
                } catch (Throwable t) {
                    //将异常保留下来
                    this.t = t;
                }
            }

            public void rethrow() {
                if (t != null) {
                    ;//处理异常
                }
            }
        }
        //任务线程
        TimedTask timerTask;
        Thread taskThread = new Thread(timerTask = new TimedTask());
        taskThread.start();
        //通过join库函数实现超时返回的功能
        taskThread.join(unit.toMillis(timeout));
        timerTask.rethrow(); //重新抛出处理过程中的异常
        ThreadUtils.printStatus(taskThread);
    }

    /*
    方法三：使用Future来取消，实际上timedRun是对future的一个简单封装
     */
    private static final ExecutorService taskExecutor = Executors.newFixedThreadPool(10);
    private static void timedRun3(Runnable r, long timeout, TimeUnit unit)
            throws InterruptedException {
        Future<?> f = taskExecutor.submit(r);
        try {
            f.get(timeout, unit);
        } catch (TimeoutException e) {
            //检测到超时，最后将被中断
        } catch (ExecutionException e) {
            /*
            通过getCause获取任务中的异常，有3种情况：
            （1）Error，应继续抛出；
            （2）Checked异常，包装成runtimeException抛出；
            （3）RuntimeException，直接抛出
             */
            throw launderThrowable(e.getCause());
        } finally {
            f.cancel(true);
        }
    }

    private static RuntimeException launderThrowable(Throwable t) {
        if (t instanceof RuntimeException) {
            return (RuntimeException)t;
        } else if (t instanceof Error) {
            throw (Error)t;
        } else {
            throw new IllegalStateException(t);
        }
    }


    public static void main(String[] args) throws InterruptedException{
        timedRun2(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {

                }
            }
        }, 1, TimeUnit.SECONDS);
    }
}
