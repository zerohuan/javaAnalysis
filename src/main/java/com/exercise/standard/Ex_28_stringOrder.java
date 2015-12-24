package com.exercise.standard;

import com.exercise.util.SwapUtil;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 全排序问题
 * 输入一个字符串,按字典序打印出该字符串中字符的所有排列。例如输入字符串abc,则打印出由字符a,b,c
 * 所能排列出来的所有字符串abc,acb,bac,bca,cab和cba。 结果请按字母顺序输出。
 * Created by yjh on 15-12-11.
 */
public class Ex_28_stringOrder {
    /*
    回溯法的应用
     */
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

    public ArrayList<String> Permutation1(String str) {
        ArrayList<String> res = new ArrayList<>();

        if (str != null && str.length() > 0) {

        }

        return res;
    }

    public static void main(String[] args) {
        System.out.println(new Ex_28_stringOrder().Permutation("abc"));
    }

}
