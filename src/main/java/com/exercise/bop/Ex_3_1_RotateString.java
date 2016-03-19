package com.exercise.bop;

/**
 * 字符串的移位包含问题
 *
 * Created by lenovo on 2016/3/12.
 */
public class Ex_3_1_RotateString {
    public static boolean rotateContain(String s1, String s2) {
        if (s1 != null && s2 != null) {
            s1 += s1;
            return s1.contains(s2);
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(rotateContain("ABCD", "DCAB"));
    }
}
