package com.yjh.inner;

/**
 * Created by yjh on 15-10-13.
 */
public class A {
    public static int a = 1;
    static {
        System.out.println("A initialized");
    }

    static class B {
        public static int b = 1;
        static {
            System.out.println("B initialized");
        }
    }

    public static int testI() {
        int i = 0;
        return ++i;
    }


    public static void main(String[] args) throws Exception {
        //初始化A时，并不会初始化A的静态内部类
        Class.forName("com.yjh.inner.A");
//        new A();
        Class<?> bc = B.class; //不会引起初始化
        int b = B.b; //会引起初始化
        System.out.println(testI());
    }
}
