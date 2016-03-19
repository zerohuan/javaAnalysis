package com.exercise.bop;

/**
 * n!�����Ʊ�ʾ���λ1��λ��
 * ʵ��������ʽ�ֽ��2��ָ��+1��
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

    //2�ĸ��� = N - N�����Ʊ�ʾ��1����Ŀ��
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
