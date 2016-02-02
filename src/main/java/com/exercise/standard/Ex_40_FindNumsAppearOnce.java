package com.exercise.standard;

import java.util.Arrays;

/**
 * 一个整型数组里除了两个数字之外，其他的数字都出现了两次。请写程序找出这两个只出现一次的数字。
 *
 * 解法：
 * 该题可以先考虑只有一个只出现一次的数字，那么所有的数异或起来得到的结果就是那个数，因为其他的数都消调了。
 * 再考虑两个的情况，关键是分开两个数（设为a，b）组分别包含一个只出现一次的数。
 *
 * 这里先全部异或得到t = a ^ b因为它们不相等因此一定不为0；
 * 那么t的二进制表示中一个为1的位必然表示这两个数的二进制表示在这个位上一个为1一个为0；
 * 用这个位将原数组区分为两类数，因为其他数都是成对的，所以必然可以将a和b分开。
 *
 * 关键词：利用二进制特点，位运算
 *
 * Created by yjh on 16-2-2.
 */
public class Ex_40_FindNumsAppearOnce {
    public static void FindNumsAppearOnce(int[] array, int num1[], int num2[]) {
        int len;
        if (array == null || (len = array.length) < 2)
            return;
        int t = 0;
        for (int i = 0; i < len; ++i) {
            t ^= array[i];
        }
        int fBit1 = findFirstBit1Index(t);
        num1[0] = num2[0] = 0;
        for (int i = 0; i < len; ++i) {
            if (isBit1(array[i], fBit1)) {
                num1[0] ^= array[i];
            } else {
                num2[0] ^= array[i];
            }
        }
    }

    private static int findFirstBit1Index(int num) {
        int index = 0;
        while ((num & 1) == 0 && index < 32) {
            num >>= 1;
            ++index;
        }
        return index;
    }

    private static boolean isBit1(int num, int index) {
        num >>= index;
        return (num & 1) == 1;
    }


    public static void main(String[] args) {
        int[] array, num1 = new int[1], num2 = new int[1];
        FindNumsAppearOnce(array = new int[]{2,4,3,6,3,2,5,5}, num1, num2);
        System.out.println(Arrays.toString(num1));
        System.out.println(Arrays.toString(num2));
    }
}
