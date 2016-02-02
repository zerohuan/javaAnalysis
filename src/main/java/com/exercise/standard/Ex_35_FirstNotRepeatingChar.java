package com.exercise.standard;

/**
 * 在一个字符串(1<=字符串长度<=10000，全部由字母组成)中找到第一个只出现一次的字符的位置。
 * 若为空串，返回-1。位置索引从0开始。
 *
 * 解法：
 * 此类问题，包括统计某些字符是否出现或者出现次数的。可以考虑使用哈希表用较小的空间换取时间效率的提升。
 *
 * Created by yjh on 16-2-1.
 */
public class Ex_35_FirstNotRepeatingChar {
    public int FirstNotRepeatingChar(String str) {
        if (str == null || str.equals(""))
            return -1;
        int[] hashTable = new int[256]; //asc码字符范围
        char[] cs =  str.toCharArray();
        for (char c : cs)
            ++hashTable[c];
        for (int i = 0; i < cs.length; ++i)
            if (hashTable[cs[i]] == 1)
                return i;
        return -1;
    }
}
