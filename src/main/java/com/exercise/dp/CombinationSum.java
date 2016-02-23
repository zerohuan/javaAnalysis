package com.exercise.dp;

/**
 * 合并总数分析
 * 原题
 * 求正数数组内和为指定数字的合并总数
 * 比如[5, 5, 10, 2, 3] 合并值为 15 : 有4种 ： (5 + 10, 5 + 10, 5 + 5 + 2 + 3, 10 + 2 + 3)
 *
 * 解法：
 * 动态规划解法
 * 状态转移方程：
 * dp[0][0] = 1;
 * dp[n][m] = dp[n-1][m] + dp[n-1][m-num[n-1]];
 * dp[n][m]表示数组中前n个数中合并和为m的组合数；
 *
 * Created by yjh on 2016/2/23.
 */
public class CombinationSum {
    public static int combinationSum(int[] num, int sum) {
        int len;
        if (num == null || (len = num.length) == 0 || sum < 0)
            return 0;
        int[][] dp = new int[len+1][sum+1];
        dp[0][0] = 1;
        for (int n = 1; n <= len; n++) {
            for (int m = 0; m <= sum; m++) {
                if (m >= num[n-1])
                    dp[n][m]=dp[n-1][m]+dp[n-1][m-num[n-1]];
                else
                    dp[n][m] = dp[n-1][m];
            }
        }
        return dp[len][sum];
    }

    public static void main(String[] args) {
        System.out.println(combinationSum(new int[]{5,5,10,2,3}, 15));
    }
}
