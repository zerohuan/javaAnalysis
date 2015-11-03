package com.exercise;

/**
 * Created by yjh on 15-11-3.
 */
public class NumberOf1Between1AndN {
    public static int NumberOf1Between1AndN_Solution(int n) {
        int count = 0;
        int t = 1;
        while(n / t > 0) {
            int post = n % t;
            t *= 10;
            int prev = n / t;
            int cur = (n % t) / (t / 10);


            if(cur == 0 && prev >= 1) {
                prev--;
                post = 0;
            }
            if(prev != 0)
                prev++;
            if(cur == 1)
                count += prev * (t / 10) + (post == 0 ? 0 : post + 1);
        }

        return count;
    }

    public static void main(String[] args) {
        System.out.println(NumberOf1Between1AndN_Solution(20));
    }
}
