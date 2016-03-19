package com.exercise.bat;

/**
 * 小米
 *
 * 风口之下，猪都能飞。当今中国股市牛市，真可谓“错过等七年”。 给你一个回顾历史的机会，已知一支股票连续n天的价格走势，
 * 以长度为n的整数数组表示，数组中第i个元素（prices[i]）代表该股票第i天的股价。 假设你一开始没有股票，但有至多两次买入1
 * 股而后卖出1股的机会，并且买入前一定要先保证手上没有股票。若两次交易机会都放弃，收益为0。
 * 设计算法，计算你能获得的最大收益。 输入数值范围：2<=n<=100,0<=prices[i]<=100
 *
 * 解法：动态规划
 *
 *
 * Created by lenovo on 2016/3/12.
 */
public class CalculateMax {
    public static int calculateMax(int[] prices) {
        int len;
        if (prices == null || (len = prices.length) == 0)
            return 0;
        int[] p = new int[len - 1];
        for (int i = 0; i < p.length; ++i)
            p[i] = prices[i+1] - prices[i];
        int sum = 0, max = 0, subMax = 0;
        for (int i = 0; i < p.length; ++i) {
            boolean flag = sum > 0;
            sum += p[i];
            if (sum <= 0) {
                sum = 0;
            } else {
                if (sum > max) {
                    int temp = max;
                    max = sum;
                    if (!flag && temp > subMax)
                        subMax = temp;
                } else if (sum > subMax) {
                    subMax = sum;
                }
            }
        }
        System.out.println(max + " " + subMax);
        return max + subMax;
    }

    public static void main(String[] args) {
        System.out.println(calculateMax(new int[]{5,15,56,26,62,65,57,69}));
    }
}
