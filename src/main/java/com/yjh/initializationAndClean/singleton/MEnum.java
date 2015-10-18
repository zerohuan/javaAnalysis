package com.yjh.initializationAndClean.singleton;

import java.lang.reflect.Constructor;

/**
 * Created by yjh on 15-10-18.
 */
public enum  MEnum {
    E1;

    static class A {
        private A(){
        }
    }

    public static void main(String[] args) throws Exception {
        Class<A> a = A.class;
        Constructor constructor = a.getDeclaredConstructor();
        constructor.setAccessible(true);
        constructor.newInstance();
        Class<?> ec = MEnum.class;
        Constructor constructor1 = ec.getDeclaredConstructor(String.class, int.class);
        constructor1.setAccessible(true);
        constructor1.newInstance("YJH", 2);
    }

}
