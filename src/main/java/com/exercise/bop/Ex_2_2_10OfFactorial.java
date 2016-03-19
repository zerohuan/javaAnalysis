package com.exercise.bop;

/**
 * 求N!中末尾有多少0
 * Created by yjh on 2016/3/15.
 */
public class Ex_2_2_10OfFactorial {
    public static int count10(int n) {
        int ret = 0;
        for (int i = 1; i <= n; ++i) {
            int j = i;
            while (j % 5 == 0) {
                ret++;
                j /= 5;
            }
        }
        return ret;
    }

    public static int count10_2(int n) {
        int ret = 0;
        while (n > 0) {
            ret += n / 5;
            n /= 5;
        }
        return ret;
    }

    public static void main(String[] args) {
        System.out.println(count10_2(10));
    }
}
