package com.concurrent.examples.memorizer;

import java.util.concurrent.TimeUnit;

/**
 * Created by yjh on 15-12-24.
 */
public class ExpensiveFunction implements Computable<String,Integer> {
    @Override
    public Integer compute(String arg) throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        return 0;
    }
}
