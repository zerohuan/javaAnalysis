package com.exercise;

/**
 * 实现一个函数，将一个字符串中的空格替换成“%20”。例如，
 * 当字符串为We Are Happy.则经过替换之后的字符串为We%20Are%20Happy。
 *
 * 思路：
 * 太简单.
 *
 * Created by yjh on 15-10-25.
 */
public class StringReplace {
    public String replaceSpace(StringBuffer str) {
        for(int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if(c == ' ')
                str.replace(i, i+1, "%20");
        }
        return str.toString();
    }

    public static void main(String[] args) {
        StringReplace stringReplace = new StringReplace();
        System.out.println(stringReplace.replaceSpace(new StringBuffer("We Are Happy")));
    }
}
