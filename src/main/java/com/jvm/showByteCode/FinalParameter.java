package com.jvm.showByteCode;

/**
 * 通过字节码查看final参数在class的使用，
 * 字段表的ACC_FINAL标志
 *
 * Created by yjh on 15-11-27.
 */
public final class FinalParameter {
    interface A {
        void test();
    }

    private void testFinal(byte[] bytes) {
        int a = 10;
        new A() {
            @Override
            public void test() {
                System.out.println(a + " " + bytes.length);
            }
        };
//        a = 11;
//        bytes = null;
    }
}
