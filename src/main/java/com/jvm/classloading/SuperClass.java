package com.jvm.classloading;

/**
 *
 * Created by yjh on 15-11-17.
 */
public class SuperClass {
    static {
        System.out.println("SuperClass init!");
    }
    static final int valueFinal = 123;
    static int value = 123;
}
