package com.jvm.classloading;

/**
 * 具体类的初始化不会引起接口的初始化，除非使用接口中定义的变量
 *
 * Created by yjh on 15-11-19.
 */
public class InterfaceInitialization {
    interface A {
        int a = v();
    }

    private static class I implements A {
        static {
            System.out.println("I initialization");
        }

        public static void main(String[] args) {

        }
    }

    private static int v() {
        System.out.println("v invoked");
        return 9;
    }
}
