package com.yjh.interfaceAndClass;

/**
 * Created by yjh on 15-11-4.
 */
public class AccessControlTest {
    static class A {
        private void a() {

        }

        public void b() {

        }

        protected void c() {

        }
    }

    static class B extends A {
        /*
        这个a方法并不是对父类方法的重写
         */
//        @Override
        public void a() {

        }

        /*
        你不能降低父类方法的访问权限
         */
//        protected void b() {
//
//        }
        @Override
        public void c() {

        }
    }

}
