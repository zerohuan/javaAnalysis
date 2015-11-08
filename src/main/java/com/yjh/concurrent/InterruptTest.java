package com.yjh.concurrent;

/**
 * 测试中断状态
 *
 * Created by yjh on 15-11-7.
 */
public class InterruptTest {
    public void runTest() {
        double d = 0.0;
        for(int i = 0; i < Integer.MAX_VALUE; i++) {
            d += 100 * Math.sqrt(i);
        }
    }

    public static void main(String[] args) {
        Thread thread = new Thread(() ->{
            try {
                double d = 0.0;
                for(int i = 0; i < Integer.MAX_VALUE; i++) {
                    d += 100 * Math.sqrt(i);
                    if(Thread.interrupted()) {
                        System.out.println("Thread is interrupted! d:" + d);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
        thread.interrupt();
        System.out.println("调用第1次interrupt " + thread.isInterrupted());
        thread.interrupt(); //第2次也没有用
        System.out.println("调用第2次interrupt " + thread.isInterrupted());
    }
}
