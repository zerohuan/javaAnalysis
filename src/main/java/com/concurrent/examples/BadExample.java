package com.concurrent.examples;

/**
 * Created by yjh on 15-12-19.
 */
public final class BadExample {
    static class NoVisibility implements Runnable {
        private boolean ready; //volatile
        private int number; //atomic

        @Override
        public void run() {
            while(!ready)
                Thread.yield();
            System.out.println(number);
        }

        public static void main(String[] args) throws InterruptedException {
            for(int i = 0; i < 1000; ++i) {
                NoVisibility noVisibility = new NoVisibility();
                new Thread(noVisibility).start();
                noVisibility.number = 12; //lock，防止重排序导致number在ready之后赋值
                noVisibility.ready = true; //lock
            }
        }
    }

    static class VolatileAndServerModule {
        private boolean toggle; //volatile

        public void test() {
            while(!toggle) {
                System.out.println("here");
            }
        }

        public static void main(String[] args) {
        }
    }

}
