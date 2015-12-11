package com.yjh.generic;

import java.sql.SQLException;

/**
 * Created by yjh on 15-12-6.
 */
public class GenericLimit {
    private static <T extends Throwable> void doWork() throws T {
        try {

        } catch (Throwable e) {
            throw e;
        }
    }

    private static void doW() throws Throwable {

    }

    private static class R implements Runnable {
        @Override
        public void run() {
            try {
                throw new SQLException();
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }
    }

    public static void main(String[] args) {
        new R().run();
    }
}
