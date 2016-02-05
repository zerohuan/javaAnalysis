package com.exercise.standard;

/**
 * 构建乘积数组
 * 给定一个数组A[0,1,...,n-1],请构建一个数组B[0,1,...,n-1],
 * 其中B中的元素B[i]=A[0]*A[1]*...*A[i-1]*A[i+1]*...*A[n-1]。
 * 不能使用除法。
 *
 * 解法：
 * （1）直接连乘，时间复杂度为O(n^2)，效率太低；
 * （2）注意B[i]可以分成两部分： 时间O(n)
 * C[i] = A[0] × ... × A[i-1]；
 * D[i] = A[i+1] × ... × A[n-1]；
 * 先计算C代表的部分，再计算D代表的部分；
 * 这里在计算的时候，一定要利用上一步计算结果，减少重复计算；
 *
 * Created by yjh on 16-2-4.
 */
public class Ex_52_Multiply {
    public static int[] multiply(int[] A) {
        int len;
        if (A == null || (len = A.length) == 0)
            return A;
        int[] B = new int[len];
        //计算C代表的部分
        B[0] = 1;
        for (int i = 1; i < len; ++i) {
            B[i] = B[i - 1] * A[i - 1];
        }
        //计算D代表的部分
        int temp = 1;
        for (int i = len - 2; i >= 0; --i) {
            temp *= A[i + 1];
            B[i] *= temp;
        }
        return B;
    }

    public static int[] multiply_2(int[] A) {
        int len;
        if (A == null || (len = A.length) == 0)
            return A;
        int[] B = new int[len];
        for (int i = 0; i < len; ++i) {
            B[i] = 1;
        }
        for (int i = 0; i < len; ++i) {
            for (int j = 0; j < len; ++j) {
                if (i != j)
                    B[j] *= A[i];
            }
        }
        return B;
    }


}
