package com.exercise;

import java.util.ArrayList;

/**
 * 输入两个整数序列，第一个序列表示栈的压入顺序，请判断第二个序列是否为该栈的弹出顺序。
 * 假设压入栈的所有数字均不相等。例如序列1,2,3,4,5是某栈的压入顺序，序列4,5,3,2,1是
 * 该压栈序列对应的一个弹出序列，但4,3,5,1,2就不可能是该压栈序列的弹出序列。
 *
 * 思路：
 * 这道题思路很简单，按照pushA序列的顺序将它们压入栈stack，用一个变量j记录popA的下标位置，
 * 每压入一个元素就进行下面的检查：
 * 查看栈顶元素是否与popA[j]相同，相同就说明popA序列也是在这个时候让该元素出栈的，那么就将stack pop一次，
 * j++，继续检查，直到popA遍历完，或者，popA[j]!=stack.peek();
 *
 * Created by yjh on 15-10-28.
 */
public class StackPopOrder {

    public static boolean IsPopOrder(int [] pushA,int [] popA) {
        ArrayList<Integer> stack = new ArrayList<Integer>();

        if(pushA.length == 0)
            return false;

        int j = 0;
        for(int i : pushA) {
            stack.add(i);
            while(j < popA.length && popA[j] == stack.get(stack.size()-1)) {
                stack.remove(stack.size()-1);
                j++;
            }
        }

        return stack.isEmpty();
    }

    public static boolean IsPopOrder2(int [] pushA,int [] popA) {
        ArrayList<Integer> order = new ArrayList<Integer>(pushA.length);

        for (int i : pushA) {
            order.add(i);
        }

        for(int i : popA) {
            if(order.indexOf(i) < 0)
                return false;
        }

        if(pushA.length <= 2)
            return true;
        else if(pushA.length == 0)
            return false;

        for(int i = 1; i < popA.length - 1; i++) {
            int prev = order.indexOf(popA[i - 1]);
            int cur = order.indexOf(popA[i]);
            int next = order.indexOf(popA[i + 1]);
            if(prev - cur > next - cur && next - cur > 0)
                return false;

        }

        return true;
    }

    public static void main(String[] args) {
        int[] a = {1,2,3,4,5};
        int[] b = {4,5,3,2,1};
        System.out.println(IsPopOrder(a, b));
    }
}
