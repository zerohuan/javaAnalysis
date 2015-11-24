package com.yjh.interfaceAndClass;

/**
 * Created by yjh on 15-11-22.
 */
public class InterfaceBasic {
    protected interface A {
        void a();
    }

    /*
    组合接口中的名字冲突，，可以通过组合+内部类（闭包）的方式解决
     */
    interface I1 {void f();}
    interface I2 {int f(int i);}
    interface I3 {int f();}
    class C {public int f() {return 1;}}

    class C2 implements I1, I2 {
        @Override
        public void f() {

        }

        @Override
        public int f(int i) {
            return 0;
        }
    }

     class C3 extends C implements I2 {
         //这个方法同时属于C和I2，在客户端代码中调用是，不能区分
         @Override
         public int f(int i) {
             return 0;
         }
     }

    class C4 extends C implements I3 {
        public int f() {return 1;}
    }

//    C5这种情况就无法直接实现
//    class C5 extends C implements I1 {    }

    private interface IF1 {
        int a = initInt("IF1 a");
        int b = initInt("IF1 b");
    }



    private static int initInt(String fieldName) {
        System.out.println(fieldName);
        return 2;
    }

    interface IF2 {
        public static interface  IF21 {

        }

        public static final class IC {

        }
    }

    public static void main(String[] args) {
        int v = IF1.a;
    }
}
