package com.yjh.interfaceAndClass;

import java.lang.reflect.Constructor;

/**
 * Created by yjh on 15-11-22.
 */
class A {
    A() {
        System.out.println("A");
    }
}

public class InnerClassTest extends A {
    private int t = 9;


    interface A {
        void doSomething();
    }

    public A a () {
        return new A() {
            private int a = 0;

            @Override
            public void doSomething() {
                System.out.println("doSomething " + t);
            }
        };
    }

    public InnerClassTest test() {
        return new InnerClassTest() {

        };
    }

    class Inner {
        public Inner(int x) {

        }
    }

    public static void main(String[] args) throws Exception {
        Class<?> claz = Class.forName("com.yjh.interfaceAndClass.InnerClassTest$1");
        System.out.println(claz == null);
        Constructor<?> s = claz.getDeclaredConstructor(InnerClassTest.class);
        System.out.println(s == null);
        A a = (A)s.newInstance(new InnerClassTest());
        a.doSomething();
    }

}
