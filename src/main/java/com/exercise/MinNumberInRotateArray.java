package com.exercise;

/**
 * 把一个数组最开始的若干个元素搬到数组的末尾，我们称之为数组的旋转。输入一个非递减序列的一个旋转，
 * 输出旋转数组的最小元素。例如数组{3,4,5,1,2}为{1,2,3,4,5}的一个旋转，该数组的最小值为1。
 *
 * 思路：
 * 如果发生了旋转，一定有array[i]>array[i+1];
 * 否则直接返回array[0];
 *
 * Created by yjh on 15-10-26.
 */
public class MinNumberInRotateArray {
    public int minNumberInRotateArray(int [] array) {
        if(array.length == 0)
            return 0;

        for(int i = 0; i < array.length - 1; i++) {
            if(array[i] > array[i+1])
                return array[i+1];
        }

        return array[0];
    }

    public static void main(String[] args) {
        MinNumberInRotateArray minNumberInRotateArray = new MinNumberInRotateArray();
        int[] a = {3,4,5,1,2,2};
        System.out.println(minNumberInRotateArray.minNumberInRotateArray(a));
    }
}
