package com.exercise.standard;

import java.util.Arrays;

/**
 * 问题：不用加减乘除做加法
 * 写一个函数，求两个整数之和，要求在函数体内不得使用+、-、*、/四则运算符号。
 *
 * 解法：
 * 用位运算带代替加法，用异或得到两个数二进制表示在加法中不需要进位的部分，用&得到需要进位的部分：
 * num1 + num2 = t1 + t2;
 * 不断重复（1）异或得到t1；（2）与得到t2；直到t2为0也就是没有需要进位的部分，那么t1就是最终的答案了。
 *
 * 注意：对于负数的验证
 *
 *
 * 问题：不使用新变量，交换两个变量的值
 *
 * 解法一：加减法表示；
 * 解法二：位运算表示；
 *
 * Created by yjh on 16-2-3.
 */
public class Ex_47_Add {
    public static int Add(int num1, int num2) {
        int t1, t2;
        do {
            t1 = num1 ^ num2;
            t2 = (num1 & num2) << 1;

            num1 = t1;
            num2 = t2;
        } while (num2 != 0);

        return t1;
    }

    public static int AddMy(int num1, int num2) {
        int t1 = (num1 & num2) << 1;
        int t2 = num1 ^ num2;
        int temp;
        while ((temp = t1 & t2) != 0) {
            t2 = t1 ^ t2;
            t1 = temp << 1;
        }

        return t1 ^ t2;
    }

    public static int[] swap(int[] n,int a, int b) {
        n[a] = n[a] + n[b];
        n[b] = n[a] - n[b];
        n[a] = n[a] - n[b];
        return n;
    }

    public static int[] swap_2(int[] n, int a, int b) {
        n[a] = n[a] ^ n[b];
        n[b] = n[a] ^ n[b];
        n[a] = n[a] ^ n[b];
        return n;
    }

    public static void main(String[] args) {
        System.out.println(-2 & 1);
        System.out.println(Add(-4, 3));
        System.out.println(Arrays.toString(swap_2(new int[]{1,2}, 0, 1)));
    }
}
