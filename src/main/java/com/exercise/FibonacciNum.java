package com.exercise;

/**
 * 大家都知道斐波那契数列，现在要求输入一个整数n，请你输出斐波那契数列的第n项。
 *
 * Created by yjh on 15-10-26.
 */
public class FibonacciNum {
    public int Fibonacci(int n) {
        if(n <= 1)
            return 1;
        int i,last,answer = 0,nextToLast;
        last = nextToLast = 1;

        for(i=2; i <= n; i++) {
            answer = last + nextToLast;
            nextToLast = last;
            last = answer;
        }

        return answer;
    }

    public static void main(String[] args) {
        FibonacciNum fibonacciNum = new FibonacciNum();
        System.out.println(fibonacciNum.Fibonacci(3));
    }
}
