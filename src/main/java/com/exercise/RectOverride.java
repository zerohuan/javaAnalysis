package com.exercise;

/**
 * 我们可以用2*1的小矩形横着或者竖着去覆盖更大的矩形。请问用n个2*1的小矩形无重叠地覆盖一个2*n的大矩形，总共有多少种方法？
 *
 * Created by yjh on 15-10-27.
 */
public class RectOverride {
    public static int RectCover(int target) {
        int x = 1, x2 = 1;
        int count;
        for(int i = 2; i <= target; i++) {
            count = x + x2;
            x2 = x;
            x = count;
        }
        return x;
    }


    public static void main(String[] args) {
        System.out.println(RectCover(3));
    }
}
