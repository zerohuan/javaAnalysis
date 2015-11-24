package com.jvm.classloading;

/**
 * Created by yjh on 15-11-17.
 */
public class SubClass extends SuperClass implements MInterface {
    static {
        System.out.println("SubClass init!");
    }
    {
        System.out.println("SubClass instantiation!");
    }

    public SubClass() {
        System.out.println("SubClass constructor!");
        privateMethod(); //在构造器中
    }

    @Override
    public void interfaceMethod() {

    }

    private void privateMethod() {
        System.out.println("SubClass private method");
    }

}
