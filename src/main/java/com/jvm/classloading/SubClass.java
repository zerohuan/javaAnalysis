package com.jvm.classloading;

/**
 * Created by yjh on 15-11-17.
 */
public class SubClass extends SuperClass {
    static {
        System.out.println("SuperClass init!");
    }

}
