package com.exercise.bop;

/**
 * 求二进制数中1的个数
 * Created by yjh on 2016/3/14.
 */
public class Ex_2_1_count1 {
    public static int count1(int p) {
        int count = 0;
        while (p > 0) {
            count += (p & 1);
            p >>= 1;
        }
        return count;
    }

    //时间复杂度O(M)，M为二进制表示中1的个数
    public static int count1_2(int b) {
        int count = 0;
        while (b > 0) {
            b &= (b - 1);
            count++;
        }
        return count;
    }

    //解法三，预先保存每个字节对应的1的个数，直接返回，空间换时间

    public static void main(String[] args) {
        System.out.println(count1(255));
    }
}
