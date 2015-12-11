package com.jvm.classloading;

import java.util.Arrays;

/**
 * 通过字节码查看数组的创建和长度获取
 *
 * Created by yjh on 15-11-18.
 */
public class ArrayLengthTest {
    private static class A implements Cloneable {}

    public static void main(String[] args) throws Exception {
        int[] ints = new int[10];

        int a = ints.length;
        int b = ints.length;

        int[] ints1 = ints.clone();

        A[] as = new A[10];
        int c = ints1.length;
        A[] as1 = as.clone();

        System.out.println(Arrays.toString(ints.getClass().getInterfaces()));
    }

    //字节码
//    public class com.jvm.classloading.ArrayLengthTest {
//        public com.jvm.classloading.ArrayLengthTest();
//        Code:
//        0: aload_0
//        1: invokespecial #1                  // Method java/lang/Object."<init>":()V
//        4: return
//
//        public static void main(java.lang.String[]) throws java.lang.ExceptionShow;
//        Code:
//        0: bipush        10
//        2: newarray       int
//        4: astore_1
//        5: aload_1s
//        6: arraylength
//        7: istore_2
//        8: aload_1
//        9: arraylength
//        10: istore_3
//        11: aload_1
//        12: invokevirtual #2                  // Method "[I".clone:()Ljava/lang/Object;
//        15: checkcast     #3                  // class "[I"
//        18: astore        4
//        20: bipush        10
//        22: anewarray     #4                  // class com/jvm/classloading/ArrayLengthTest$A
//        25: astore        5
//        27: aload         4
//        29: arraylength
//        30: istore        6
//        32: aload         5
//        34: invokevirtual #5                  // Method "[Lcom/jvm/classloading/ArrayLengthTest$A;".clone:()Ljava/lang/Object;
//        37: checkcast     #6                  // class "[Lcom/jvm/classloading/ArrayLengthTest$A;"
//        40: astore        7
//        42: getstatic     #7                  // Field java/lang/System.out:Ljava/io/PrintStream;
//        45: aload_1
//        46: invokevirtual #8                  // Method java/lang/Object.getClass:()Ljava/lang/Class;
//        49: invokevirtual #9                  // Method java/lang/Class.getInterfaces:()[Ljava/lang/Class;
//        52: invokestatic  #10                 // Method java/util/Arrays.toString:([Ljava/lang/Object;)Ljava/lang/String;
//        55: invokevirtual #11                 // Method java/io/PrintStream.println:(Ljava/lang/String;)V
//        58: return
//    }

}
