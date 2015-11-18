package com.jvm.classloading;

/**
 * 继承和类加载过程
 *
 * Created by yjh on 15-11-16.
 */
public class InheritAndClassLoad {
    static {
        System.out.println("Wrapper start initializing");
    }

    private static class D {
        static {
            System.out.println("D is initializing!");
        }

        static {
            System.out.println("D is initialized!");
        }
    }

    private static class A {
        static {
            System.out.println("A start initializing!");
        }
        //包含子类的static引用，并且在定义处进行实例化，子类的初始化会在父类初始化结束前进行，这可能造成错误
//        private static C ac = new C("a c");
        private static D d = new D();

        private static int sa = 9;


        int a = initInt("a instantiated.");

        static {
            System.out.println("A is initialized!");
        }

        public A() {
            System.out.println("A constructor.");
        }

        public static void main(String[] args) {
            C c = new C("main");
        }
    }

    private static class B extends A {
        static {
            System.out.println("B start initializing!");
        }

        int b = initInt("b instantiated.");

        public B() {
            System.out.println("B constructor.");
        }

        static {
            System.out.println("B is initialized!");
        }

        public static void main(String[] args) {
            try {
                Class.forName("com.jvm.classloading.InheritAndClassLoad$C");
            } catch (Exception e) {
                e.printStackTrace();;
            }
        }
    }

    private static class C extends B {
        static {
            System.out.println("C start initializing!");
        }
        private static int sc = 9 + A.sa;
        int c = initInt("c instantiated.");

        public C(String id) {
            System.out.println("C constructor. " + id);
        }

        static {
            System.out.println("C is initialized!" + sc);
        }

        public static void main(String[] args) {
            C c = new C("main");
        }
    }

    private static int initInt(String str) {
        System.out.println(str);
        return 0;
    }

    public static void main(String[] args) {
    }
}
