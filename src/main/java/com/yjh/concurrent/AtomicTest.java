package com.yjh.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 原子性操作保证
 *
 * Created by yjh on 15-11-12.
 */
public class AtomicTest {
    private static AtomicReference<CombineVariable> ac = new AtomicReference<>();
    private static CombineVariable combineVariable = new CombineVariable();
    private static AtomicStampedReference<CombineVariable> asr =
            new AtomicStampedReference<>(combineVariable, 0);
    private static CountDownLatch downLatch = new CountDownLatch(10000);


    /**
     * 一个误用的例子
     * cas成功后的语句并不能保证原子性
     */
    private static class TestABA implements Runnable {

        @Override
        public void run() {
            for(;;) {
                int oldStamp = asr.getStamp();
                CombineVariable c = asr.getReference();
                if(asr.compareAndSet(c, c, oldStamp, ++oldStamp)) {
                    c.a++;
                    c.c++;
                    c.b++;
                    downLatch.countDown();
                    break;
                }
            }
        }
    }

    public static void main(String[] args) {
        CombineVariable c1 = new CombineVariable();
        CombineVariable c2 = new CombineVariable();
        ac.set(c1);

        System.out.println(ac.compareAndSet(c1, c2));

        //测试防止aba的CAS操作
        ExecutorService service = Executors.newFixedThreadPool(10);
        for(int i = 0; i < 10000; i++) {
            service.execute(new TestABA());
        }
        service.shutdown();

        new Thread(() -> {
            try {
                downLatch.await();
                System.out.println(combineVariable);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

    }

    private static class CombineVariable {
        private int a;
        private int b;
        private int c;

        @Override
        public String toString() {
            return "CombineVariable{" +
                    "a=" + a +
                    ", b=" + b +
                    ", c=" + c +
                    '}';
        }
    }
}
