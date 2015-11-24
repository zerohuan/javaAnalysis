package com.jvm.classloading;

/**
 * Created by yjh on 15-11-19.
 */
public class ThreadAndInitialization {
    static class DeadLoopClass {
        static {
            if(true) { //加上if(true)通过编译，编译器和JVM无法判断一个程序能否在有限的时间结束，停机问题不可解
                System.out.println(Thread.currentThread() + "init DeadLoopClass");
                while (true) {

                }
            }
        }
    }

    public static void main(String[] args) {
        Runnable script = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread() + " start");
                DeadLoopClass dlc = new DeadLoopClass();
                System.out.println(Thread.currentThread() + " run over");
            }
        };

        Thread thread1 = new Thread(script);
        Thread thread2 = new Thread(script);
        thread1.start();
        thread2.start();
    }
}
