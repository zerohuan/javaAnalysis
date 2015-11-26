package com.jvm.executionengine;

/**
 * Created by yjh on 15-11-26.
 */
public class SlotReuseTest {
    /**
     * 垃圾回收不会回收当前栈帧中局部变量表引用指向的对象
     */
    private static void test1() {
        byte[] placeholder = new byte[64 * 1024 * 1024];
        System.gc();
    }

    /**
     * 由于局部变量表可以被重用，placeholder引用在作用域执行完后，仍然保存在局部变量表中，不被在当前栈帧活跃时被垃圾回收
     */
    private static void test2() {
        {
            byte[] placeholder = new byte[64 * 1024 * 1024];
        }
        System.gc();
    }

    /**
     * int a = 0;将重用placeholder的局部变量表中的slot，因此字节数组得到释放
     */
    private static void test3() {
        {
            byte[] placeholder = new byte[64 * 1024 * 1024];
        }
        int a = 0; //或者用placeholder=null代替
        System.gc();
    }

    public static void main(String[] args) {

    }
}
