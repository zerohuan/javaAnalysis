package com.yjh.initializationAndClean.singleton;

/**
 *
 *
 * Created by yjh on 15-10-18.
 */
public class SingletonWithInnerClass {
    private SingletonWithInnerClass() {
        System.out.println("initialized");
    }

    private static class SingletonHolder {
        private static final SingletonWithInnerClass s = new SingletonWithInnerClass();
    }

    public static SingletonWithInnerClass getInstance() {
        return SingletonHolder.s;
    }

    public static void main(String[] args) {
        Class c = SingletonWithInnerClass.class; //这里并没有进行初始化
        System.out.println("start initialization:");
        SingletonWithInnerClass singletonWithInnerClass = SingletonWithInnerClass.getInstance();


    }
}
