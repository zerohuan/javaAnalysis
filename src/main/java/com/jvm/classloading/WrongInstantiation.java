package com.jvm.classloading;

/**
 * Created by yjh on 15-11-24.
 */
public class WrongInstantiation {
    private abstract static class A {
        private int aInt = 9;

        public A() {
            doSomething();
//            try {
//                doPrivate();
//            } catch (ExceptionShow e) {
//                e.printStackTrace();
//            }
        }

        protected void doSomething() {
            System.out.println("A's doSomething");
        }

        private void doPrivate() throws Exception {
            System.out.println("A's doPrivate");
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
