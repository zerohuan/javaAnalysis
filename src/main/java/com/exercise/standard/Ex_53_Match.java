package com.exercise.standard;

/**
 * 正则表达式匹配
 * 请实现一个函数用来匹配包括'.'和'*'的正则表达式。模式中的字符'.'表示任意一个字符，
 * 而'*'表示它前面的字符可以出现任意次（包含0次）。 在本题中，匹配是指字符串的所有字符匹配整个模式。
 * 例如，字符串"aaa"与模式"a.a"和"ab*ac*a"匹配，但是与"aa.a"和"ab*a"均不匹配
 *
 * Created by yjh on 16-2-4.
 */
public class Ex_53_Match {


    /*
    我的写法，太乱了
     */
    public static boolean match_2(char[] str, char[] pattern) {
        if (str == null || pattern == null)
            return false;
        int strIndex = 0, pIndex = 0, strLen = str.length, pLen = pattern.length;
        while (pIndex < pLen && strIndex < strLen) {
            if (pattern[pIndex] != '.' && pattern[pIndex] != '*') {
                if (pIndex + 1 < pLen && pattern[pIndex + 1] == '*') {
                    while (strIndex < strLen && str[strIndex] == pattern[pIndex])
                        strIndex++;
                    pIndex += 2;
                    if (strIndex == strLen) {
                        boolean flag = false;
                        int temp = pIndex;
                        while (temp < pLen) {
                            if (pattern[temp] == str[strIndex - 1])
                                flag = true;
                            temp++;
                        }
                        if (flag)
                            strIndex--;
                    }
                } else {
                    if (str[strIndex++] != pattern[pIndex++])
                        return false;
                }
            } else if (pattern[pIndex] == '.') {
                if ((pIndex + 1 < pLen && pattern[pIndex + 1] != '*') || pIndex + 1 >= pLen) {
                    pIndex++;
                    strIndex++;
                } else {
                    pIndex += 2;
                    strIndex = strLen;
                    boolean flag = false;
                    int temp = pIndex;
                    while (temp < pLen) {
                        if (pattern[temp] == str[strIndex - 1])
                            flag = true;
                        temp++;
                    }
                    if (flag)
                        strIndex--;
                }
            }
        }
        if (strIndex < strLen)
            return false;
        while (pIndex < pLen && pattern[pIndex] != '*') {
            if (pIndex + 1 < pLen && pattern[pIndex + 1] == '*') {
                pIndex += 2;
            } else
                return false;
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println("aaa".matches("a*b*"));
        System.out.println(match_2("bbbba".toCharArray(), ".*a*a".toCharArray()));
    }
}
