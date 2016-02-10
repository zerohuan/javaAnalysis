package com.exercise.standard;

/**
 * 表示数值的字符串
 * 请实现一个函数用来判断字符串是否表示数值（包括整数和小数）。例如，字符串"+100","5e2","-123",
 * "3.1416"和"-1E-16"都表示数值。但是"12e","1a3.14","1.2.3","+-5"和"12e+4.3"都不是。
 *
 * 解法：
 * 这题没有太多特殊的技巧，但答案一定要清晰，逻辑性明确。
 *
 * 注意：
 * "+.","+.1","600."都是合法的；
 *
 * Created by yjh on 16-2-4.
 */
public class Ex_54_IsNumeric {
    public static boolean isNumeric(char[] str) {
        int len;
        if (str == null || (len = str.length) == 0)
            return false;
        boolean hasPoint = false;
        for (int i = 0; i < len; ++i) {
            if (i == 0) {
                if (isSym(str[i])) {
                    if (i + 1 >= len || (!is1to9(str[i+1]) && str[i+1] != '.'))
                        return false;
                } else if (!is1to9(str[i]))
                    return false;
            } else {
                if (isE(str[i])) {
                    return isInt(str, i + 1, len - 1);
                } else if (str[i] == '.') {
                    if (hasPoint) return false;
                    hasPoint = true;
                } else if (!is1to9(str[i]))
                    return false;
            }
        }

        return true;
    }

    private static boolean isInt(char[] str, int l, int r) {
        int len = r - l + 1;
        if (len > 0) {
            int i = l;
            if (isSym(str[i])) {
                System.out.println(i);
                return i < r && isAllNum(str, ++i, r);
            } else if (is1to9(str[i])) {
                return (i < r && isAllNum(str, ++i, r)) || i == r;
            }
        }
        return false;
    }

    private static boolean isAllNum(char[] str, int l, int r) {
        if (l > r)
            return false;
        for (int i = l; i <= r; ++i) {
            if (!is1to9(str[i])) {
                return false;
            }
        }
        return true;
    }

    private static boolean isSym(char c) {
        return c == '-' || c == '+';
    }

    private static boolean is1to9(char c) {
        return c >= '0' && c <= '9';
    }

    private static boolean isE(char c) {
        return c == 'e' || c == 'E';
    }

    public static void main(String[] args) {
        String str;
        System.out.println(isInt((str = "+6").toCharArray(), 0, str.length() - 1));
        System.out.println(isNumeric("100".toCharArray()));
    }
}
