package com.jvm.showByteCode;

/**
 *
 * Created by yjh on 16-1-13.
 */
public final class LambdaShow {
    private static class A {
        public void doWork(B b) {
            System.out.println("prev");
            b.doSomething();
            System.out.println("post");
        }
    }
    private interface B {
        void doSomething();
    }
    private static void lambdaByteCodeShow() {
        new A().doWork(()->System.out.println("lambda"));
    }

    public static void main(String[] args) {
        lambdaByteCodeShow();
    }
}
