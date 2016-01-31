package com.exercise.standard;

/**
 * 连续子序列最大和问题
 * 注意可能包含负数；
 * Created by yjh on 16-1-26.
 */
public class Ex_31_FindGreatestSumOfSubArray {
    /*
    解法一：根据题目特点采用联机算法
     */
    public static int findGreatestSumOfSubArray_1(int[] array) {
        if (array == null || array.length == 0)
            return 0;
        int sum = array[0], max = sum;
        for (int i = 1; i < array.length; ++i) {
            if (sum < 0) //如果sum小于0，直接变成下一个
                sum = array[i];
            else //sum大于0，累加下一个
                sum += array[i];
            if (sum > max)
                max = sum;
        }
        return max;
    }

    /*
    解法二：基于动态规划
    递归式:用一个数组f保存每一步的最优解
    f[i] = (1) array[i]; //i == 0 || f[i-1] <= 0
           (2) f[i-1] + array[i] //i != 0 && f[i-1] > 0
     */
    public static int findGreatestSumOfSubArray_2(int[] array) {
        if (array == null || array.length == 0)
            return 0;
        int len;
        int[] f = new int[len = array.length];
        f[0] = array[0];
        for (int i = 1; i < len; ++i) {
            if (f[i - 1] <= 0)
                f[i] = array[i];
            else
                f[i] = f[i - 1] + array[i];
        }
        int max = f[0];
        for (int i = 1; i < len; ++i)
            if (f[i] > max)
                max = f[i];
        return max;
    }



    public static void main(String[] args) {
        int[] array = new int[]{-2,-8,-1,-5,-9};
        System.out.println(findGreatestSumOfSubArray_2(array));
    }
}
