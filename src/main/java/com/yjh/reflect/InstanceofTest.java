package com.yjh.reflect;

/**
 * Created by yjh on 15-12-8.
 */
public class InstanceofTest {
    private static class A {

    }

    private static class B extends A {

    }

    public static void main(String[] args) {
        A a = new A();
        B b = new B();
        Class<A> ac1 = A.class;
        Class<?> aClass = a.getClass();
        Class<?> bClass = b.getClass();
        System.out.println(a instanceof A);
        System.out.println(b instanceof A);
        System.out.println(aClass.isInstance(b));
        System.out.println(aClass.isInstance(a));
        System.out.println(null instanceof A);
        System.out.println(aClass.isInstance(null));
    }
}
