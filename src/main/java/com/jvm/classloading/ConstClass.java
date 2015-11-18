package com.jvm.classloading;

/**
 * Created by yjh on 15-11-17.
 */
public class ConstClass {
    static {
        System.out.println("ConstClass init!");
    }

    static final String HELLOWORLD = "hello world";
}
