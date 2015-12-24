package com.exercise.backtracking;

import java.util.Arrays;

/**
 * 八皇后问题：
 * 在一个8×8的国际象棋上摆放8个皇后，使其不能相互攻击，求一共有多少中摆放方法；
 *
 * 思路：回溯法，扩展到n皇后问题
 * （1）用一个向X=(x1,x2,x3,...,xn)表示解空间中的一个解，x1表示第一个皇后在第1行第x1列上；
 * （2）任意两个皇后i，j，满足：xi != xj（不在同一列）；|xi - xj| != |i - j|（不再同一对角线）；
 *
 * Created by yjh on 15-12-21.
 */
public final class EightQueen {
    //迭代解法
    private static boolean isValid(int[] x, int k) {
        for (int i = 0; i < k; ++i)
            if (x[i] == x[k] || Math.abs(x[i] - x[k]) == Math.abs(i - k)) return false;
        return true;
    }

    private static int inQueen(int n) {
        //解向量
        int[] x = new int[n];
        int k = 0, totalCount = 0;
        while (k >= 0) {
            ++x[k]; //放置皇后
            while (x[k] <= n && !isValid(x, k)) //检查与之前的放置的皇后是否冲突
                ++x[k];
            if (x[k] <= n && k == n - 1) { //找到一个解
                totalCount++;
                System.out.println(Arrays.toString(x));
            }
            else {
                if (x[k] > n) --k; //回溯
                else x[++k] = 0; //放置下一个皇后，0表示尚未放置
            }
        }
        return totalCount;
    }

    //递归解法
    private static int rnQueen(int[] x, int k) {
        int n, total = 0;
        if (k == (n = x.length)) {
            //找到一个解
            System.out.println(Arrays.toString(x));
            total++;
        }
        else {
            for (int i = 1; i <= n; i++) {
                x[k] = i;
                if (isValid(x, k))
                    total += rnQueen(x, k + 1);
            }
        }
        return total;
    }

    public static void main(String[] args) {
        System.out.println(inQueen(8));
        System.out.println(rnQueen(new int[8], 0));
    }
}
