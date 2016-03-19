package com.exercise.bop;

/**
 * 最大公约数的几种方法
 * Created by yjh on 2016/3/15.
 */
public class Ex_2_7_GCD {
    //辗转相除法
    public static int gcd(int x, int y) {
        return y == 0 ? x : gcd(y, x % y);
    }

    //用减法代替mod
    public static int gcd2(int x, int y) {
        if (x > y)
            return gcd2(y,x);
        if (y == 0)
            return x;
        else
            return gcd2(x - y, y);
    }

    //减少递归次数
    public static int gcd3(int x, int y) {
        if (y > x)
            return gcd3(y,x);
        if (y == 0)
            return x;
        if (isEven(x)) {
            if (isEven(y))
                return 2 * gcd3(x >> 1, y >> 1);
            else
                return gcd3(x >> 1, y);
        } else {
            if (isEven(y))
                return gcd3(x, y >> 1);
            else
                return gcd3(y, x - y);
        }
    }

    public static boolean isEven(int x) {
        return (x & 1) == 1;
    }

    public static void main(String[] args) {
        System.out.println(gcd3(2, 10));
    }
}
