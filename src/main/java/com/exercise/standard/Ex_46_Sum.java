package com.exercise.standard;

/**
 * 求1+2+3+...+n，要求不能使用乘除法、for、while、if、else、switch、case等关键字及条件判断语句（A?B:C）。
 *
 * 解法：
 * 利用短路的特性+递归
 *
 * Created by yjh on 16-2-3.
 */
public class Ex_46_Sum {
    public static int Sum_Solution(int n) {
        int ans = n;
        boolean bl = ans > 0 && (ans += Sum_Solution(n - 1)) > 0;
        return ans;
    }

    public static void main(String[] args) {
        System.out.println(Sum_Solution(4));
    }
}
