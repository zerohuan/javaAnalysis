package com.jvm.classloading;

/**
 * Created by yjh on 15-11-22.
 */
public class WrapClassAndStaticInner {
    static {
        System.out.println("wrapper initializing!");
    }

    private static class Inner {
        static {
            System.out.println("Inner initializing!");
        }

        public static void main(String[] args) {

        }
    }
}
