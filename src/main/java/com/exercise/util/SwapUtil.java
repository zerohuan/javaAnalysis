package com.exercise.util;

/**
 * Created by yjh on 15-12-20.
 */
public class SwapUtil {
    public static void swap(char[] cs, int i, int j) {
        if (i != j) {
            char temp = cs[i];
            cs[i] = cs[j];
            cs[j] = temp;
        }
    }

    public static void swap(int[] cs, int i, int j) {
        if (i != j) {
            int temp = cs[i];
            cs[i] = cs[j];
            cs[j] = temp;
        }
    }

    public static void reverse(char[] seq, int start) {
        int len;
        if(seq == null || (len = seq.length) <= start)
            return;
        for (int i = 0; i < ((len - start) >> 1); i++) {
            int p = start + i, q = len - 1 - i;
            if (p != q)
                SwapUtil.swap(seq, p, q);
        }
    }

    public static void reverse(int[] seq, int start) {
        int len;
        if(seq == null || (len = seq.length) <= start)
            return;
        for (int i = 0; i < ((len - start) >> 1); i++) {
            int p = start + i, q = len - 1 - i;
            if (p != q)
                SwapUtil.swap(seq, p, q);
        }
    }

    public static boolean checkInvalidArray(int[] n) {
        return n == null || n.length == 0;
    }
}
