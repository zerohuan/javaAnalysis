package com.exercise;

/**
 * 给定一个double类型的浮点数base和int类型的整数exponent。求base的exponent次方。
 *
 * 思路：快速简单幂
 *
 * 4 ^ 9 = 4 ^ (1001) = 4 ^ (1000) + 4 ^ 1 = 4 ^ 8 + 4 ^ 1，
 * 而8 = 2 ^ 3，可见具体和exponent的二进制表示有关；
 *
 * Created by yjh on 15-10-27.
 */
public class DoublePow {
    public double Power(double base, int exponent) {
        int n;
        if(exponent > 0) {
            n = exponent;
        } else {
            n = - exponent;
        }
        double r = 1.0;
        while(n != 0) {
            if((n & 1) == 1) r *= base;
            base *= base;
            n >>= 1;
        }
        return exponent > 0 ? r : 1 / r;
    }

    public double Power2(double base, int exponent) {
        if(exponent >= 0) {
            if(exponent == 0)
                return 1;
            if(exponent == 1)
                return base;
            if(exponent % 2 == 0)
                return Power(base, exponent >> 1) * Power(base, exponent >> 1);
            else
                return base * Power(base, exponent >> 1) * Power(base, exponent >> 1);
        } else {
            return 1 / Power(base, -exponent);
        }
    }

    public static void main(String[] args) {
        DoublePow pow = new DoublePow();

        System.out.println(pow.Power2(2, -1));
    }
}
