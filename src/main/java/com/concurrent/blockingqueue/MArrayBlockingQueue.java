package com.concurrent.blockingqueue;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * Created by yjh on 15-12-22.
 */
public class MArrayBlockingQueue<E> {
    private final Object[] items;
    final ReentrantLock lock;
    private final Condition notEmpty;
    private final Condition notFull;

    private int tailIndex;
    private int headIndex;
    private int count;

    public MArrayBlockingQueue(int capacity, boolean fair) {
        if(capacity < 0)
            throw new IllegalArgumentException();
        items = new Object[capacity];
        lock = new ReentrantLock(fair);
        notEmpty = lock.newCondition();
        notFull = lock.newCondition();
    }

    public void put(E e) throws InterruptedException {
        final Lock lock = this.lock;
        lock.lockInterruptibly();
        try {
            int len;
            while (count == (len = items.length)) { //队列已满等待
                notFull.await();
            }
            items[tailIndex] = e;
            if(++tailIndex == len) //不用mod
                tailIndex = 0;
            count++;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }


    public E take() throws InterruptedException {
        final Lock lock = this.lock;
        lock.lockInterruptibly();
        try {
            while (count == 0) {
                notEmpty.await();
            }
            @SuppressWarnings("unchecked")
            E e = (E)items[headIndex];
            items[headIndex] = null; //一定不要忘了置空，否则可能造成内存泄漏
            if (++headIndex == items.length)
                headIndex = 0;
            --count;
            notFull.signal();
            return e;
        } finally {
            lock.unlock();
        }
    }

    public int size() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            return count;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        MArrayBlockingQueue<String> queue = new MArrayBlockingQueue<>(10, true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int i = 0;
                    while (i < 100) {
                        queue.put(String.valueOf(++i));
                        TimeUnit.MILLISECONDS.sleep(500);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        String str = queue.take();
                        System.out.println(str + " " + queue.count);
                        TimeUnit.MILLISECONDS.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }


}
