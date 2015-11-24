package com.jvm.classloading;

/**
 * Created by yjh on 15-11-24.
 */
public class WrongInstantiation {
    private static class A {
        private int aInt = 9;

        public A() {
            doSomething();
        }

        protected void doSomething() {
            System.out.println("A's doSomething");
        }
    }

    private static class B extends A {
        private int bInt = 9;

        @Override
        protected void doSomething() {
            System.out.println("B's doSomething, bInt: " + bInt);
        }
    }

    public static void main(String[] args) {
        B b = new B();
    }
    /**
     * B's doSomething, bInt: 0
     */
}
