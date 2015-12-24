package com.jvm.showByteCode;

/**
 * Created by yjh on 15-12-23.
 */
public class ReturnShow {
    private static int returnAndIinc() {
        int i = 3;
        /*
        自增对应指令Iinc，局部表量表自增
        由于自增操作操作数栈没有改变，return还是返回操作数栈顶元素
         */
        return i++;
    }
    /**
     * 0: iconst_3
     1: istore_0
     2: iload_0
     3: iinc          0, 1
     6: ireturn

     */


    private static int returnAnd() {
        int i = 3;
        /*
        这里编译器会将通过load指令将局部变量表的最新的值加载到操作数栈
         */
        return ++i;
    }
    /**
     * 0: iconst_3
     1: istore_0
     2: iinc          0, 1
     5: iload_0
     6: ireturn

     */
}
