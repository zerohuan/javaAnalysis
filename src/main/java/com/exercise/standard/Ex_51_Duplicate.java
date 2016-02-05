package com.exercise.standard;

import com.exercise.util.SwapUtil;

import java.util.Arrays;

/**
 * 数组中重复的数字
 * 在一个长度为n的数组里的所有数字都在0到n-1的范围内。 数组中某些数字是重复的，但不知道有几个数字是重复的。
 * 也不知道每个数字重复几次。请找出数组中任意一个重复的数字。 例如，如果输入长度为7的数组{2,3,1,0,2,5,3}，
 * 那么对应的输出是重复的数字2或者3。
 *
 * 解法：
 * 这里一定要利用题目中的信息，“长度为n的数组”，“都在0到n-1的范围内”。
 *
 * Created by yjh on 16-2-3.
 */
public class Ex_51_Duplicate {
    public static boolean duplicate(int numbers[],int length,int [] duplication) {
        int len;
        boolean flag = false;
        if (numbers != null && (len = numbers.length) > 0) {
            for (int i = 0; i < len; ++i) {
                if (i != numbers[i]) {
                    if (numbers[numbers[i]] == numbers[i]) {
                        duplication[0] = numbers[i];
                        flag = true;
                    } else {
                        SwapUtil.swap(numbers, i, numbers[i]);
                    }
                }
            }
        }
        return flag;
    }

    public static void main(String[] args) {
        int[] n = new int[1];
        System.out.println(duplicate(new int[]{2,1,3,1,4}, 0, n));
        System.out.println(Arrays.toString(n));
    }
}
