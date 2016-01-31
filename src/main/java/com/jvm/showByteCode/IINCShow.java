package com.jvm.showByteCode;

/**
 * ++i 和 i++：
 * 它们都是直接对局部变量表中的值，而不改变操作数栈的值
 * 两者的区别就在于前者是在变量加载到操作数之前增加，而后者是在加载到操作数栈之后自增
 * 因此i = i++; 的局部变量表中i的值：先变为0,再变为1,再变为0；
 * Created by yjh on 16-1-14.
 */
public class IINCShow {
    private static void test1() {
        int i = 0;
        i += i++; //0
    }

    private static void test2() {
        int i = 0;
        i = i++; //0
    }

    private static void test3() {
        int i = 0;
        i = ++i; //1
    }

    private static void test4() {
        int i = 0;
        i += ++i; //1
    }

    public static void main(String[] args) {
        int count=0;
        for(int i=0;i<=100;i++){
            System.out.println(count += count++);
        }
    }
}
