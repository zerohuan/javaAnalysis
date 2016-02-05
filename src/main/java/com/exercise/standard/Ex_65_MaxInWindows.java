package com.exercise.standard;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

/**
 * 滑动窗口的最大值
 * 给定一个数组和滑动窗口的大小，找出所有滑动窗口里数值的最大值。例如，
 * 如果输入数组{2,3,4,2,6,2,5,1}及滑动窗口的大小3，那么一共存在6个滑动窗口，
 * 他们的最大值分别为{4,4,6,6,6,5}； 针对数组{2,3,4,2,6,2,5,1}的滑动窗口有以下6个：
 * {[2,3,4],2,6,2,5,1}， {2,[3,4,2],6,2,5,1}， {2,3,[4,2,6],2,5,1}，
 * {2,3,4,[2,6,2],5,1}， {2,3,4,2,[6,2,5],1}， {2,3,4,2,6,[2,5,1]}。
 *
 * 解法：
 *
 * Created by yjh on 16-2-5.
 */
public class Ex_65_MaxInWindows {
    /*
    我的分析：
    滑动窗口从左向右移动的过程，可以看成数组中的数，按顺序输入/输出一个固定大小的队列的过程。
    也就是窗口是一个队列；但是同时要“实时”的得到窗口内的最大，也就是说在保持队列特点的同时还要具有最大堆的特点。
    那我的直接的想法是用链表实现队列的同时，每个节点增加两个引用left，right构建最大堆。
    但是这种方法显然比较复杂，而且时间是O(nlogk);
     */

    /*
    利用双端队列实现
     */
    public static ArrayList<Integer> maxInWindows(int [] num, int size) {
        ArrayList<Integer> res = new ArrayList<>();
        int len;

        if (num != null && (len = num.length) > 0 && size > 0) {
            Deque<Integer> deque = new LinkedList<>();
            for (int i = 0; i < size; ++i) {
                while (!deque.isEmpty() && num[i] >= num[deque.getLast()])
                    deque.pollLast();
                //存入下标
                deque.offer(i);
            }
            for (int i = size; i < len; ++i) {
                res.add(num[deque.peek()]);
                while (!deque.isEmpty() && num[i] >= num[deque.getLast()])
                    deque.pollLast();
                if (!deque.isEmpty() && deque.peek() <= i - size)
                    deque.pop();
                deque.offer(i);
            }
            res.add(num[deque.peek()]);
        }
        return res;
    }

    public static void main(String[] args) {
        System.out.println(maxInWindows(new int[]{2,3,4,2,6,2,5,1}, 3));
    }
}
