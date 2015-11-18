package com.basic.util.testUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

/**
 * 自定义测试器，使用策略模式
 *
 * Created by yjh on 15-11-17.
 */
public final class MTester {
    private Logger logger = LogManager.getLogger();

    private static final MTester TESTER = new MTester();

    public void doTest(MTestable mTestable) {
        String name = "";
        if(mTestable instanceof AbstractTestable)
            name = ((AbstractTestable) mTestable).getName();
        long start = System.currentTimeMillis();
        logger.info("start test " + name);
        try {
            mTestable.test();
        } catch (Exception t) {
            logger.error(t);
        }
        long end = System.currentTimeMillis();
        logger.info("end test " + name);
        logger.info("take time " + (end - start)  + "毫秒\n");
    }

    public static void test(MTestable mTestable) {
        TESTER.doTest(mTestable);
    }

    public static void main(String[] args) {
        MTester.test(new AbstractTestable("元测试") {
            @Override
            public void test() throws Exception {
                TimeUnit.SECONDS.sleep(1);
            }
        });
    }
}
