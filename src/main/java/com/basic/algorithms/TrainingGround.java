package com.basic.algorithms;

import java.util.Arrays;

/**
 * 我的草稿纸
 *
 * Created by yjh on 15-11-25.
 */
public class TrainingGround {
    public static void temp(int[] input) {

        int k = 10, n;
        if(input == null || (n = input.length) == 0) {
            return;
        }

        int[] b = new int[input.length];
        int[] c = new int[k];
        int i;
        for(i = 0; i < n; i++)
            c[input[i]]++;

        for(i = 1; i < k; i++) {
            c[i] += c[i - 1];
        }

        for(i = n - 1; i >= 0; --i)
            b[--c[input[i]]] = input[i];

        for(i = 0; i < n; i++)
            input[i] = b[i];
        System.out.println(Arrays.toString(input));
    }

    public static void main(String[] args) {
        int[] data = {5,3,7,2,6,2,1,8,3};
        temp(data);
    }

}
