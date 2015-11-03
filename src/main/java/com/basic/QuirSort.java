package com.basic;

/**
 * Created by yjh on 15-11-3.
 */
public class QuirSort {
    static void quickSort(int[] nums, int l, int h) {
        if(nums == null)
            return;
        if(l < h) {

        }
    }

    static int partition(int[] num, int l, int h) {
        int x = num[h];
        int j = -1, i = 0;
        while(i < h) {
            if(num[i] < x) {
                j++;
                if(i != j) {
                    int temp = num[i];
                    num[i] = num[j];
                    num[j] = temp;
                }
            }
        }
        return j;
    }
}
