package com.yjh.interfaceAndClass;

/**
 * 类的复用
 *
 * Created by yjh on 15-11-16.
 */
public class ReuseClass {
    private interface I {
        void i();
    }

    private interface R {
        void i();
    }

    private abstract static class A {
        public abstract void i();
    }

    private static class B extends A implements I, R {
        //可以用内部类来模拟多重继承解决意外的“重名”
        @Override
        public void i() {

        }
    }

    //冲突的方法签名
    public static class RI {
        private interface I {
            void i();
        }

        private interface R {
            int i();
        }

        private abstract static class A {
            public abstract void i();
        }

        private static class B extends RI.A implements RI.I {
            //可以用内部类来模拟多重继承解决意外的“重名”
            @Override
            public void i() {
            }

            public RI.R makeR() {
                return new RI.R() {
                    @Override
                    public int i() {
                        return 0;
                    }
                };
            }
        }
    }


}
