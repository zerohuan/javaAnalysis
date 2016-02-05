package com.exercise.standard;

/**
 * 问题：翻转单词顺序列
 * 输入一个英文句子，翻转句子中单词的顺序，但单词内字符顺序不变。
 * 为简单期间，标点符号和普通字母一样处理。例如输入字符串“I am a student.”，
 * 则输出“student. a am I”
 *
 * 解法：
 * 两次翻转法，先翻转整个句子，再翻转句子中的每个单词。
 *
 * 问题：左旋转字符串
 * 利用两次翻转的思想，将字符串分为两部分：
 * 一是要左旋的n个字符；
 * 二是剩下的len - n个字符；
 * 那么同样可以先翻转整个，在分别翻转两个部分得到结果。
 *
 * Created by yjh on 16-2-3.
 */
public class Ex_42_ReverseSentence {
    public static String ReverseSentence(String str) {
        int len;
        if (str == null || (len = str.length()) == 0)
            return "";
        char[] cs = str.toCharArray();
        //翻转整个句子
        reverse(cs, 0, len - 1);
        //在翻转每个单词
        int begin = 0, end = begin;
        while (begin < len) {
            if (cs[begin] == ' ') {
                begin++;
                end++;
            } else if (end == len || cs[end] == ' ') {
                reverse(cs, begin, --end);
                begin = ++end;
            } else
                ++end;
        }

        return new String(cs);
    }
    private static void reverse(char[] cs, int l, int h) {
        while (l < h) {
            char t = cs[l];
            cs[l] = cs[h];
            cs[h] = t;
            ++l;
            --h;
        }
    }

    public static String LeftRotateString(String str,int n) {
        int len;
        if (str == null || (len = str.length()) == 0)
            return "";
        if (n <= 0 || n >= len)
            return str;
        char[] cs = str.toCharArray();
        reverse(cs, 0, len - 1);
        reverse(cs, 0, len - n - 1);
        reverse(cs, len - n, len - 1);
        return new String(cs);
    }

    public static void main(String[] args) {
        System.out.println(LeftRotateString("Wonderful", 2));
    }
}
