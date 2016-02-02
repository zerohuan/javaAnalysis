package com.exercise.standard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 输入一个正整数数组，把数组里所有数字拼接起来排成一个数，打印能拼接出的所有数字中最小的一个。
 * 例如输入数组{3，32，321}，则打印出这三个数字能排成的最小数字为321323。
 *
 * 解法：
 * 实际上就是对数组中的数按照某种规则排序，使得最后连起来得到的数最小；
 * 设有数组中两个数n1和n2，只要比较num(str(n1)+str(n2))和num(str(n2)+str(n1))的大小就可以知道这
 * 两个数在最终序列中的顺序。
 *
 * （1）如果直接使用int，要注意溢出的问题；
 * （2）可以先将数组转存成字符串List，利用Collections.sort和自定义Comparator；
 *
 * 注意要掌握这种方法的证明：
 * 首先这是一种比较关系，因此必须满足：自反性，对称性，传递性；
 * 之后用反证法证明：存在序列中两个数：AX，AY，
 * A1...AX...AY...AN（基于比较排序得到的结果） > A1...AY...AX...AN是不可能的；
 *
 * Created by yjh on 16-2-1.
 */
public class Ex_33_PrintMinNumber {
    public static String printMinNumber(int [] numbers) {
        int len;
        if (numbers == null || (len = numbers.length) == 0)
            return "";
        sort(numbers);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; ++i)
            sb.append(numbers[i]);
        return sb.toString();
    }
    private static void sort(int[] numbers) {
        int len = numbers.length;
        int i,j;
        for (i = 1; i < len; ++i) {
            int temp = numbers[i];
            for (j = i; j > 0 && compare(numbers[j - 1], temp) > 0; --j) {
                numbers[j] = numbers[j - 1];
            }
            numbers[j] = temp;
        }
    }
    private static int compare(int x, int y) {
        double n1 = x * Math.pow(10, bitOfNum(y)) + y;
        double n2 = y * Math.pow(10, bitOfNum(x)) + x;
        if (n1 < n2)
            return -1;
        else if (n1 > n2)
            return 1;
        else
            return 0;
    }
    public static int bitOfNum(int x) {
        int i = 0;
        while (x > 0) {
            i++;
            x /= 10;
        }
        return i;
    }

    public static String printMinNumber_2(int [] numbers) {
        if (numbers == null || numbers.length == 0)
            return "";
        Comparator<String> comparator = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                String str1 = o1 + o2;
                String str2 = o2 + o1;
                return str1.compareTo(str2);
            }
        };
        List<String> nStrs = new ArrayList<>();
        for (int i : numbers)
            nStrs.add(String.valueOf(i));
        Collections.sort(nStrs, comparator);
        StringBuilder sb = new StringBuilder();
        for (String str : nStrs) {
            sb.append(str);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(compare(3334, 3333332));
        System.out.println(printMinNumber_2(new int[]{3334, 3, 3333332}));
    }
}
