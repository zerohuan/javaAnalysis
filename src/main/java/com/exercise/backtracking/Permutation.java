package com.exercise.backtracking;

import com.exercise.util.SwapUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * 全排序问题
 * 输入一个字符串,按字典序打印出该字符串中字符的所有排列。例如输入字符串abc,则打印出由字符a,b,c
 * 所能排列出来的所有字符串abc,acb,bac,bca,cab和cba。 结果请按字母顺序输出。
 * Created by yjh on 15-12-11.
 */
public class Permutation {
    /**
     * 递归解法
     * @param str 输入字符串
     * @return
     */
    public ArrayList<String> Permutation(String str) {
        ArrayList<String> res = new ArrayList<>();

        if (str != null && str.length() > 0) {
            PermutationHelper(str.toCharArray(), 0, res);
            Collections.sort(res);
        }

        return res;
    }

    private static void PermutationHelper(char[] cs, int i, ArrayList<String> list) {
        if(i == cs.length - 1) { //解空间的一个叶节点
            list.add(String.valueOf(cs)); //找到一个解
        } else {
            for(int j = i; j < cs.length; ++j) {
                if(j == i || cs[j] != cs[i]) {
                    SwapUtil.swap(cs, i, j);
                    PermutationHelper(cs, i + 1, list);
                    SwapUtil.swap(cs, i, j); //复位
                }
            }
        }
    }

    /**
     * 迭代解法：字典生成算法
     * @param str 输入排列
     * @return
     */
    public static ArrayList<String> Permutation1(String str) {
        ArrayList<String> res = new ArrayList<>();

        if (str != null && str.length() > 0) {
            char[] seq = str.toCharArray();
            Arrays.sort(seq); //排列
            res.add(String.valueOf(seq)); //先输出一个解

            int len = seq.length;
            while (true) {
                int p = len - 1, q;
                //从后向前找一个seq[p - 1] < seq[p]
                while (p >= 1 && seq[p - 1] >= seq[p]) --p;
                if (p == 0) break; //已经是“最小”的排列，退出
                //从p向后找最后一个比seq[p]大的数
                q = p; --p;
                while (q < len && seq[q] > seq[p]) q++;
                --q;
                //交换这两个位置上的值
                SwapUtil.swap(seq, q, p);
                //将p之后的序列倒序排列
                reverse(seq, p + 1);
                res.add(String.valueOf(seq));
            }
        }

        return res;
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

    public static void main(String[] args) {
        System.out.println(new Permutation().Permutation("abc"));
        char[] cs;
        reverse(cs = "abcdef".toCharArray(), 1);
        System.out.println(cs);
        System.out.println(Permutation1("aabc"));
    }

}
