package com.jvm.showByteCode;

/**
 *
 * Created by yjh on 15-12-6.
 */
public class ArrayShow {
    static private class A {

    }

    static private class B extends A {

    }

    private static void test(A[][] as) {
        if(as != null)
            System.out.println(as.length);
    }

    private static void testA(A a) {
        B b  = (B)a;
        System.out.println(b);
    }

    private static void reflectArray() throws ClassNotFoundException {
        A[][] as = new A[10][10];
        int[] ints = new int[10];
        System.out.println(as.getClass().getCanonicalName());
        A a = as[0][0];
        Class<?> claz = as.getClass();
//        Class<?> clazz = Class.forName("[[Lcom.jvm.showByteCode.ArrayShow$A");
    }

    public static void main(String[] args) throws Exception {
        reflectArray();
        A a = new A();
        String s = new String("132");
        new ExceptionShow();
    }
}
