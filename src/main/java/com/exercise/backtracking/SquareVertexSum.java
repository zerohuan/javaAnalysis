package com.exercise.backtracking;

import com.exercise.util.SwapUtil;

import java.util.Arrays;

/**
 * 输入含有8个数字的数组，判断有没有可能把这8个数字分别放到正方体的8个顶点上，
 * 使得正方体上三组相对的面上的4个顶点的和都相等；
 * 思路：
 * 全排序+验证
 * Created by yjh on 15-12-23.
 */
public class SquareVertexSum {
    private static boolean isValid(int[] n) {
        return (n[0] + n[1] + n[2] + n[3]) == (n[4] + n[5] + n[6] + n[7])
                && (n[0] + n[2] + n[4] + n[6]) == (n[1] + n[3] + n[5] + n[7])
                && (n[0] + n[1] + n[4] + n[5]) == (n[2] + n[3] + n[6] + n[7]);
    }

    //递归解法
    private static boolean rsolve(int[] n, int k) {
        int len;
        if (k == (len = n.length)) {//一个可能的解验证
            boolean flag;
            if (flag = isValid(n)) System.out.println(Arrays.toString(n));
            return flag;
        }
        else {
            for (int i = 0; i < len; ++i) {
                if (i == k || n[i] != n[k]) {
                    SwapUtil.swap(n, i, k);
                    if (rsolve(n, k + 1)) //找到一个解，结束
                        return true;
                    SwapUtil.swap(n, i, k);
                }
            }
        }
        return false;
    }

    //迭代解法
    private static boolean isolve(int[] n) {
        int len;
        if (n != null && (len = n.length) == 8) {
            Arrays.sort(n); //排序，从这个排列开始向后穷举
            while (true) {
                int p = len - 1, q;
                //向后查找小于n[p]的第一个数
                while (p > 0 && n[p - 1] >= n[p]) --p;
                //再向后找到最后一个大于n[p]的数
                q = p + 1;
                while (q < len && n[q] > n[p]) ++q;
                --q;
                //交换两个数
                SwapUtil.swap(n, p, q);
                //将p之后的数字序列倒序排列，得到下一个数字排列
                SwapUtil.reverse(n, p + 1);
                if (isValid(n)) { //验证是否符合条件
                    System.out.println(Arrays.toString(n));
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(rsolve(new int[] {1,1,1,1,1,1,1,1}, 0));
        System.out.println(isolve(new int[] {1,1,1,1,1,1,1,1}));
    }
}
