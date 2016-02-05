package com.exercise.standard;

/**
 * 把字符串转换成整数
 * 将一个字符串转换成一个整数，要求不能使用字符串转换整数的库函数。
 *
 * 解法：
 * 注意输入检查：
 * （1）为null和空字符串，返回0；是否抛出异常？
 * （2）"+"和"-"的处理；
 *
 * Created by yjh on 16-2-3.
 */
public class Ex_49_StrToInt {
    public static int StrToInt(String str) {
        int len;
        if (str == null || (len = str.length()) == 0)
            return 0;
        char[] cs = str.toCharArray();
        int t = 1, res = 0;
        for (int i = len - 1; i >= 0; --i) {
            boolean f;
            if ((f = !isNum(cs[i])) && res != 0)
                return 0;
            else if (!f) {
                if (cs[i] != '-' && cs[i] != '+') {
                    res += t * (cs[i] - '0');
                    t *= 10;
                } else if (cs[i] == '-'){
                    res = -res;
                }
            }

        }
        return res;
    }
    private static boolean isNum(char c) {
        return ('0' < c && c < '9') || c == '-' || c == '+';
    }

    public static void main(String[] args) {
        System.out.println(StrToInt("+123"));
    }
}
