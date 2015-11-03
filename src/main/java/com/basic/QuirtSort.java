package com.basic;

import java.util.Arrays;

/**
 * Created by yjh on 15-11-3.
 */
public class QuirtSort {
    static void quickSort(int[] num, int l, int h) {
        if(num == null)
            return;
        if(l < h) {
            int m = partition(num, l, h);
            quickSort(num, l, m - 1);
            quickSort(num, m + 1, h);
        }
    }

    static int partition(int[] num, int l, int h) {
        int x = num[h];
        int j = l-1, i = l;
        while(i < h) {
            if(num[i] < x) {
                j++;
                if(i != j) {
                    swap(num, i, j);
                }
            }
            i++;
        }
        if(j != h)
            swap(num, ++j, h);
        return j;
    }

    static void swap(int[] num, int i, int j) {
        int temp = num[i];
        num[i] = num[j];
        num[j] = temp;
    }

    public static void main(String[] args) {
        int[] num = {5,3,4,7,8,2,4,54,73};
        quickSort(num, 0, num.length - 1);
        System.out.println(Arrays.toString(num));
    }
}
