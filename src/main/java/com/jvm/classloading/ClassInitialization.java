package com.jvm.classloading;

/**
 * Created by yjh on 15-11-19.
 */
public class ClassInitialization {
    static {
        i = 9;
//        System.out.println(i); //不能访问之后的，这是“非法向前引用”
    }
    private static int i = 1; //最终的值是1,i的值的变化：准备(0)—>静态语句块(9)->赋值动作(1)

    public static void main(String[] args) {
        System.out.println(i);
    }
}
