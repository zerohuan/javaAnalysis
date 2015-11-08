package com.exercise;

/**
 * Created by yjh on 15-11-5.
 */
public class Solution2 {
    public int NumberOf1Between1AndN_Solution(int n) {
        if(n <= 0)
            return 0;
        return num1Count(String.valueOf(n));
    }

    public int num1Count(String num) {
        int firstChar = Integer.valueOf(num.substring(0, 1));
        if(num.equals("0"))
            return 0;
        if(num.length() == 1)
            return 1;
        String subStr = num.substring(1, num.length());
        int count = 0;
        if(firstChar > 1) {
            count += ((int)Math.pow(10, num.length() - 1));
        } else {
            count += Integer.valueOf(subStr) + 1;
        }
        count += firstChar * (num.length() - 1) * ((int)Math.pow(10, num.length() - 2));
        subStr = String.valueOf(Integer.valueOf(subStr));
        return count += num1Count(subStr);
    }

    public static void main(String[] args) {
        Solution2 solution2 = new Solution2();
        System.out.println(solution2.NumberOf1Between1AndN_Solution(10000));
    }
}