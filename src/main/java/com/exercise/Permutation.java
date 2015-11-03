package com.exercise;

/**
 * 分解问题简单化
 * 输入一个字符串,按字典序打印出该字符串中字符的所有排列。例如输入字符串abc,则打印出由
 * 字符a,b,c所能排列出来的所有字符串abc,acb,bac,bca,cab和cba。 结果请按字母顺序输出。
 *
 * 输入一个字符串,长度不超过9(可能有字符重复),字符只包括大小写字母。
 *
 * 思路：
 * 将字符串组合分成两部分：第一个字符和剩下n-1个字符串，问题就等价于一个字符+(n - 1)个字符组合；
 *
 * 注意：
 * 注意重复字符的处理；
 *
 * 总结：
 * （1）回溯法；
 * （2）类似的问题还有：八皇后问题、8个数是否可以在正方体8个顶点上并且相对两个面顶点上的数和相等；
 * （3）可以先的得出所有排列，再判断是否满足条件；
 *
 * Created by yjh on 15-11-2.
 */
import java.util.ArrayList;

public class Permutation {
    static StringBuilder sb = new StringBuilder();
    public static ArrayList<String> Permutation(String str) {
        ArrayList<String> res = new ArrayList<String>();

        if(str != null && str.length() > 0) {
            if(str.length() == 1) {
                res.add(String.valueOf(str));
            } else {
                char prev = 0;
                char[] cs = str.toCharArray();
                for(int i = 0; i < cs.length; ++i) {
                    char c = cs[i];
                    if(prev != c) {
                        sb.setLength(0);
                        sb.append(str);
                        ArrayList<String> rc = Permutation(sb.deleteCharAt(i).toString());
                        for(String s : rc) {
                            sb.setLength(0);
                            res.add(sb.append(c).append(s).toString());
                        }
                        prev = c;
                    }
                }
            }
        }

        return res;
    }

    //用stack代替递归
    public static ArrayList<String> Permutation2(String str) {
        ArrayList<String> res = new ArrayList<String>();



        return res;
    }

    public static void main(String[] args) {
        System.out.println(Permutation("abc"));
        System.out.println(Permutation("aabc"));
        System.out.println(Permutation("aa"));
    }
}
