package com.exercise.bop;

/**
 * n!二进制表示最低位1的位置
 * 实际上是因式分解后2的指数+1；
 * Created by yjh on 2016/3/15.
 */
public class Ex_2_2_factorialLowestOne {
    public static int lowestOne1(int n) {
        int ret = 0;
        while (n > 0) {
            n >>= 1;
            ret += n;
        }
        return ret + 1;
    }

    //2的个数 = N - N二进制表示中1的数目；
    public static int lowestOne2(int n) {
        int p = n, count = 0;
        while (p > 0) {
            count += p & 1;
            p >>= 1;
        }
        return n - count + 1;
    }

    public static void main(String[] args) {
        System.out.println(lowestOne2(3));
    }
}
