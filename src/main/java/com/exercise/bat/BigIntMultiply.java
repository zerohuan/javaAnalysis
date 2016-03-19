package com.exercise.bat;

/**
 * 大数相乘
 * Created by yjh on 2016/3/18.
 */
public class BigIntMultiply {
    public static String multiply(String num1, String num2) {
        int[] result = new int[num1.length() + num2.length()];
        char[] n1 = char2Num(num1.toCharArray());
        char[] n2 = char2Num(num2.toCharArray());

        for (int i = 0; i < n1.length; ++i) {
            for (int j = 0; j < n2.length; ++j) {
                result[i+j] += n1[i] * n2[j];
            }
        }
        //处理进位
        for (int i = 0; i < result.length; ++i) {
            if (result[i] > 10) {
                result[i+1] += result[i] / 10;
                result[i] %= 10;
            }
        }
        StringBuilder buffer = new StringBuilder();
        boolean flag = true;
        for (int i = result.length - 1; i >= 0; --i) {
            if (result[i] == 0 && flag)
                continue;
            else
                flag = false;
            buffer.append(result[i]);
        }
        return buffer.toString();
    }

    private static char[] char2Num(char[] num) {
        //首先高低位反转
        int l = num.length >> 1;
        for (int i = 0; i < l; ++i) {
            int end;
            char temp = num[i];
            num[i] = num[end = num.length - i - 1];
            num[end] = temp;
        }
        for (int i = 0; i < num.length; ++i) {
            num[i] -= 48; //'0' - 48 = 0
        }
        return num;
    }

    public static void main(String[] args) {
        System.out.println(multiply("12312312424","1234353534543534"));
    }
}
