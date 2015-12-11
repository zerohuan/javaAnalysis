package com.jvm.showByteCode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 * Created by yjh on 15-12-7.
 */
public class InvokeShow {

    private static interface B {
        void b();
    }

    private static class A {
        public int f() {
            return 1;
        }

        public void invokeMethod(B b) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
            Method method = b.getClass().getDeclaredMethod("b");
            method.invoke(b);
        }
    }

    public static void main(String[] args)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Class<?> clazz = A.class;
        Method method = clazz.getDeclaredMethod("f");
        method.invoke(new A());

        new A().invokeMethod(new B() {
            @Override
            public void b() {
                System.out.println("xxxx");
            }
        });
    }
}
