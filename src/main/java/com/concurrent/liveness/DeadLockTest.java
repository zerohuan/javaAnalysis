package com.concurrent.liveness;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by yjh on 15-12-23.
 */
public class DeadLockTest {
    /*
    死锁简单形式：Dead Embrace
    如果所有的线程都按照固定的顺序获取锁，那就不会发生死锁
     */
    private final Object left = new Object();
    private final Object right = new Object();
    private void leftRight() throws InterruptedException {
        synchronized (left) {
            TimeUnit.MILLISECONDS.sleep(100);
            synchronized (right) {
                System.out.println("left-right");
            }
        }
    }

    private void rightLeft() throws InterruptedException  {
        synchronized (right) {
            TimeUnit.MILLISECONDS.sleep(100);
            synchronized (left) {
                System.out.println("right-left");
            }
        }
    }

    private static void rlDeadLock() {
        DeadLockTest t = new DeadLockTest();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    t.leftRight();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    t.rightLeft();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /*
    动态的锁顺序死锁
    依赖与运行时变量引用的对象顺序
     */
    private static final class Account {
        final Lock lock = new ReentrantLock();
        double balance;
        void dedit(double amount) {}
        void credit(double amount) {}
    }
    //from和to依赖与参数顺序，可能会造成死锁
    private void transferMoney(Account from, Account to, double amount) {
        synchronized (from) {
            synchronized (to) {
                if (from.balance < amount)
                    throw new IllegalStateException();
                from.dedit(amount);
                from.dedit(amount);
            }
        }
    }

    /*
    避免动态的锁顺序问题造成的死锁问题：根据具体对象确定锁顺序，System.identityHashCode(o);
    当hash值相同时，使用一个“加时赛”锁来消除死锁的可能
     */
    private final Object tieLock = new Object();
    private void transferMoney1(Account from, Account to, double amount) {
        class Helper {
            void transfer() {
                if (from.balance < amount)
                    throw new IllegalStateException();
                from.dedit(amount);
                to.credit(amount);
            }
        }
        int fromHash = System.identityHashCode(from);
        int toHash = System.identityHashCode(to);
        if (fromHash < toHash) {
            synchronized (from) {
                synchronized (to) {
                    new Helper().transfer();
                }
            }
        }
        else if (fromHash > toHash) {
            synchronized (to) {
                synchronized (from) {
                    new Helper().transfer();
                }
            }
        }
        //hashCode相等，即发生冲突，使用额外的一个“加时赛”锁来消除死锁的可能性
        else {
            synchronized (tieLock) {
                synchronized (from) {
                    synchronized (to) {
                        new Helper().transfer();
                    }
                }
            }
        }
    }

    //使用显式锁实现轮询锁避免死锁问题
    private static final Random rnd = new Random(47); //Random是线程安全的类
    public boolean transfer(Account fromAct, Account toAct,
                            double amount, long timeout, TimeUnit unit) throws InterruptedException {
        //产生随机的等待时间重试时间，防止活锁问题
        long fixedDelay = getFixedDelayComponentNanos(timeout, unit);
        long rndDelayMod = getRandomDelayModulesNanos(timeout, unit);
        long stopTime = System.nanoTime() + unit.toNanos(timeout);
        while (true) {
            if (fromAct.lock.tryLock()) {
                try {
                    if (toAct.lock.tryLock()) {
                        try {
                            //成功获取了两个锁
                            if (fromAct.balance < amount)
                                throw new IllegalStateException();
                            fromAct.dedit(amount);
                            toAct.credit(amount);
                            return true;
                        } finally {
                            toAct.lock.unlock();
                        }
                    }
                } finally {
                    fromAct.lock.unlock();
                }
            }
            if (System.nanoTime() > stopTime)
                return false;
            //每次都加入一定的随机性
            TimeUnit.NANOSECONDS.sleep(fixedDelay + rnd.nextLong() % rndDelayMod);
        }
    }
    private static long getFixedDelayComponentNanos(long timeout, TimeUnit unit) {
        return unit.toNanos(timeout / 100);
    }
    private static long getRandomDelayModulesNanos(long timeout, TimeUnit unit) {
        return unit.toNanos(Math.max(timeout / 1000, Math.min(1000, timeout)));
    }


    public static void main(String[] args) {
        rlDeadLock();
    }
}
