package com.jvm.executionengine;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * Created by yjh on 15-11-26.
 */
public class MethodHandleTest {
    static class ClassA {
        public void println(String s) {
            System.out.println("classA: " + s);
        }
    }

    private static MethodHandle getPrintMH(Object receiver) throws Exception {
        MethodType mt = MethodType.methodType(void.class, String.class);

        return MethodHandles.lookup().findVirtual(receiver.getClass(), "println", mt).bindTo(receiver);
    }

    static class Test {
        class GrandFather {
            void thinking() {
                System.out.println("I'm a grandfather");
            }
        }

        class Father extends GrandFather {
            @Override
            void thinking() {
                System.out.println("I'm a father");
            }
        }

        class Son extends Father {
            @Override
            void thinking() {
                try {
                    MethodType mt = MethodType.methodType(void.class);
                    MethodHandle mh = MethodHandles.lookup().findSpecial(GrandFather.class,
                            "thinking", mt, getClass());
                    mh.invoke(this);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }

        public static void main(String[] args) {
            (new Test().new Son()).thinking();
        }
    }

    public static void main(String[] args) throws Throwable {
        Object obj = System.currentTimeMillis() % 2 == 0? System.out : new ClassA();

        getPrintMH(obj).invokeExact("sfsf");
    }
}
