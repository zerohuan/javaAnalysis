package com.yjh.initializationAndClean.singleton;

/**
 * Created by yjh on 15-10-18.
 */
public class ConstructorTest {
    private int a;
    private String s;

    public ConstructorTest() {
    }

    public ConstructorTest(int a) {
        this.a = a;
    }

    public ConstructorTest(int a, String s) {
        this(a);
        this.s = s;
    }
}
