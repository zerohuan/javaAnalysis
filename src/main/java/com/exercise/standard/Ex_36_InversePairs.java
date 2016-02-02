package com.exercise.standard;


import java.util.Arrays;

/**
 * 在数组中的两个数字，如果前面一个数字大于后面的数字，则这两个数字组成一个逆序对。
 * 输入一个数组，求出这个数组中的逆序对的总数。
 *
 * 解法：
 * 可以考虑归并排序，可以在归并排序的合并过程中统计逆序对的顺序。
 *
 * 关键词：
 * 分治法，归并排序，辅助空间；
 *
 * Created by yjh on 16-2-1.
 */
public class Ex_36_InversePairs {
    private static int count = 0;
    private static int[] temp;
    public static int InversePairs(int[] array) {
        int len;
        if (array == null || (len = array.length) == 0)
            return 0;
        temp = new int[len];
        mergeSort(array, 0, len - 1);
        return count;
    }
    private static void mergeSort(int[] array, int l, int r) {
        if (l < r) {
            int mid = (l + r) >> 1;
            mergeSort(array, l, mid);
            mergeSort(array, mid + 1, r);
            merge(array, mid, l, r);
        }
    }
    private static void merge(int[] array, int k, int l, int r) {
        int i = l, j = k + 1, index = l;
        while (i <= k && j <= r) {
            if (array[i] <= array[j]) {
                temp[index++] = array[i++];
            } else {
                count += k - i + 1;
                temp[index++] = array[j++];
            }
        }
        int tl;
        if (i <= k) {
            tl = k - i + 1;
            System.arraycopy(array, i, array, r - tl + 1, tl);
        } else
            tl = r - j + 1;
        System.arraycopy(temp, l, array, l, r - l + 1 - tl);
    }

    public static void main(String[] args) {
        int[] array = new int[]{6,5,4,3,2,1};
        System.out.println(InversePairs(array));
        System.out.println(Arrays.toString(array));
    }
}
