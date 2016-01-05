package com.concurrent;

import java.util.concurrent.TimeUnit;

/**
 * Java语言规范定义wait，interrupt，notification三者的交互关系
 * Created by yjh on 16-1-4.
 */
public class WaitAndInterruptAndNotify {
    private static final Object mutex = new Object();
    /*
    基于M的内置锁进行实验
     */
    private static class M {
        private boolean flag = false;

        public synchronized void f1() {
            try {
                while (!flag) {
                    wait();
                }
                System.out.println(Thread.currentThread() + " normal exit");
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread() + " interrupted!");
            }
        }

        public synchronized void f2() {
            flag = true;
            notify();
        }

        public synchronized void f3() {
            flag = true;
            notifyAll();
        }

        public static void main(String[] args) throws InterruptedException {
            class T extends Thread {
                private final M m;

                public T(M m) {
                    this.m = m;
                }

                @Override
                public void run() {
                    m.f1();
                }
            }
            M m = new M();
            //3个线程阻塞在f1
            T t1, t2, t3;
            (t1 = new T(m)).start();
            (t2 = new T(m)).start();
            (t3 = new T(m)).start();
            TimeUnit.SECONDS.sleep(1);
            synchronized (m) {
                //中断t1，因为main所在线程还没有释放锁，所以t1不会立即抛出异常
                System.out.println(t1 + " state: " + t1.getState());
                TimeUnit.SECONDS.sleep(1);
                t2.interrupt();
                TimeUnit.SECONDS.sleep(1);
                //notify
                m.f2();
                System.out.println("main notify!");
            }
        }
    }
}
