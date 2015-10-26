package com.exercise;

import java.util.Stack;

/**
 * 用两个栈来实现一个队列，完成队列的Push和Pop操作。 队列中的元素为int类型。
 *
 * 思路：
 * （1）始终保持一个活跃的栈，入队时将元素压入该栈；
 * （2）出队时:
 * （3）当非活跃栈为空时，将活跃栈中的元素逐个出栈压入另一个栈中，直到最后一个出栈的即为出队元素；
 * （4）当非活跃栈不为空时，直接由不活跃栈出栈；
 *
 * Created by yjh on 15-10-26.
 */
public class QueueByList {
    Stack<Integer> stack1 = new Stack<Integer>();
    Stack<Integer> stack2 = new Stack<Integer>();

    public void push(int node) {
        stack1.push(node);
    }

    public int pop() {
        int res = -1;
        if(!stack2.empty()) {
            res = stack2.pop();
        } else {
            while(!stack1.empty()) {
                res = stack1.pop();
                stack2.push(res);
            }
            res = stack2.pop();
        }

        return res;
    }

    public static void main(String[] args) {
        QueueByList queueByList = new QueueByList();
        queueByList.push(1);
        queueByList.push(2);
        queueByList.push(3);
        queueByList.push(4);
        System.out.println(queueByList.pop());
        System.out.println(queueByList.pop());
        System.out.println(queueByList.pop());
        System.out.println(queueByList.pop());

    }
}
