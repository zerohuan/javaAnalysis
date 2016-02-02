package com.exercise.standard;

/**
 * Created by yjh on 16-1-31.
 */
public class Ex_32_NumberOf1Between1AndN {
    public int NumberOf1Between1AndN_Solution(int n) {
        int count=0;
        int i=1;
        for(i=1;i<=n;i*=10)
        {
            //i表示当前分析的是哪一个数位

            int a = n/i,b = n%i;
            int temp = (a%10==1) ? 1 : 0;
            count=count+(a+8)/10*i+ temp*(b+1);
        }
        return count;
    }

//    public int NumberOf1Between1AndN_Solution_1(int n) {
//
//    }

    public static void main(String[] args) {

    }
}
