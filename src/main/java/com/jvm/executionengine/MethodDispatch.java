package com.jvm.executionengine;

import java.io.Serializable;

/**
 * 方法调用，静态分派与动态分派
 *
 * Created by yjh on 15-11-26.
 */
public class MethodDispatch {
    static class StaticDispatch {
        //静态分派
        static abstract class Human {

        }

        static class Man extends Human {

        }

        static class Woman extends Human {

        }

        public void sayHello(Human human) {
            System.out.println("hello, guy!");
        }

        public void sayHello(Man man) {
            System.out.println("hello, man!");
        }

        public void sayHello(Woman weman) {
            System.out.println("hello, woman!");
        }

        /**
         * 方法重载是静态分派的应用，参数类型有变量的静态类型决定
         */
        public static void main(String[] args) {
            StaticDispatch dispatch = new StaticDispatch();
            Human man = new Man();
            Human woman = new Woman();

            dispatch.sayHello(man);
            dispatch.sayHello(woman);
        }
    }

    //静态分派与[可变]参数列表
    //尤其是使用字面值参数的“模糊性”
    public static class Overload {
        public static void sayHello(Object arg) {
            System.out.println("hello, object");
        }

        public static void sayHello(int arg) {
            System.out.println("hello, int");
        }

        public static void sayHello(long arg) {
            System.out.println("hello, long");
        }

        public static void sayHello(Character arg) {
            System.out.println("hello, character");
        }

        public static void sayHello(char arg) {
            System.out.println("hello, char");
        }

        public static void sayHello(char...arg) {
            System.out.println("hello, char ...");
        }

        public static void sayHello(Serializable arg) {
            System.out.println("hello, serializable");
        }

        public static void main(String[] args) {
            sayHello('a');
        }
    }

    static class DynamicDispatch {
        static abstract class Human {
            protected abstract void sayHello();
        }

        static class Man extends Human {
            @Override
            protected void sayHello() {
                System.out.println("Hello, man");
            }
        }

        static class Woman extends Human {
            @Override
            protected void sayHello() {
                System.out.println("Hello, woman");
            }
        }

        public static void main(String[] args) {
            Human man = new Man();
            Human woman = new Woman();

            man.sayHello();
            woman.sayHello();

            man = new Woman();
            man.sayHello();
        }
    }


}
