package com.exercise;

/**
 * 一只青蛙一次可以跳上1级台阶，也可以跳上2级。求该青蛙跳上一个n级的台阶总共有多少种跳法。
 *
 * 思路：本质上来说这也是一个动态规划的问题：
 * 对于第n步f(n)，它的上一步只有两种可能：一是1个台阶的步子；二是两个台阶的步子；
 * 因此可以得到，f(n-1)+f(n-2)，也可以看出是Fibonacci数列；
 *
 * 可以用递归，因为看出是动态规划，也可以用额外变量通过迭代进行；
 *
 * Created by yjh on 15-10-26.
 */
public class FrogStep {
    public static int JumpFloor(int target) {
        if(target == 1)
            return 1;
        if(target == 0)
            return 0;
        if(target == 2)
            return 2;
        return JumpFloor(target - 1) + JumpFloor(target - 2);
    }

    public static void main(String[] args) {
        System.out.println(JumpFloor(4));
    }
}
