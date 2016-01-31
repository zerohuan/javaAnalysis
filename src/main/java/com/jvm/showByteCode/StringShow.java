package com.jvm.showByteCode;

/**
 * Created by yjh on 16-1-12.
 */
public class StringShow {
    private String s = "";

    public static void main(String[] args) {
        String a = "13213";
        StringShow s = new StringShow();
        String str = s.s;
    }
}
