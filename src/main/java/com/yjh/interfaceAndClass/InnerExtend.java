package com.yjh.interfaceAndClass;

/**
 * Created by yjh on 15-11-23.
 */
public class InnerExtend extends InnerClassTest.Inner {
    public InnerExtend(InnerClassTest test, int x) {
        test.super(x);
    }

    public static void main(String[] args) {
        new InnerExtend(new InnerClassTest(), 1);
    }
}
