package com.concurrent.lock;

import java.util.concurrent.TimeUnit;

/**
 * Created by yjh on 16-1-12.
 */
public class FinalTest {
    private static FinalTest obj;
    private final int i;
    public FinalTest() {
        try {
            obj = this;
            TimeUnit.SECONDS.sleep(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        i = 9;
    }

    public static void main(String[] args) throws InterruptedException {

        new Thread(new Runnable() {
            @Override
            public void run() {
                new FinalTest();
            }
        }).start();
        TimeUnit.MILLISECONDS.sleep(500);
        if (obj != null)
            System.out.println(obj.i);
    }
}
