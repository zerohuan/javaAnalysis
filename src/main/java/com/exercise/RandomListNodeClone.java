package com.exercise;

import java.util.HashMap;
import java.util.Stack;

/**
 * 复杂链表复制问题
 * 输入一个复杂链表（每个节点中有节点值，以及两个指针，一个指向下一个节点，另一个特殊指针指向任意一个节点）。
 *
 * 思路：
 * （1）复制
 */
public class RandomListNodeClone {
    public RandomListNode Clone(RandomListNode pHead) {
        if (pHead == null) {
            return null;
        } else {
            Stack<RandomListNode> s = new Stack<RandomListNode>();
            while (pHead != null) {
                s.push(pHead);
                pHead = pHead.next;
            }
            RandomListNode prev = null;
            HashMap<RandomListNode, RandomListNode> map = new HashMap<RandomListNode, RandomListNode>();
            RandomListNode node = null;
            while (!s.isEmpty()) {
                RandomListNode p = s.pop();
                node = new RandomListNode(p.label);
                node.random = p.random;
                if (prev != null) {
                    node.next = prev;
                }
                map.put(p, node);
                prev = node;
            }
            RandomListNode temp = node;
            while (temp != null) {
                if (temp.random != null)
                    temp.random = map.get(temp.random);
            }

            return node;
        }
    }

    static class RandomListNode {
        int label;
        RandomListNode next = null;
        RandomListNode random = null;

        RandomListNode(int label) {
            this.label = label;
        }
    }

    public static int MoreThanHalfNum_Solution(int [] array) {
        if(array == null)
            return 0;
        HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
        int max = 0;
        for(int i = 0; i < array.length; i++) {
            Integer t = map.get(array[i]);
            if(t == null) {
                map.put(array[i],t = 1);
            } else {
                map.put(array[i],++t);
            }
            if(t > (array.length >> 1)) {
                return array[i];
            }
            if(max < t)
                max = t;
            if(array.length - 1 - i + max < (array.length >> 1))
                return 0;
        }

        return 0;
    }

    public static void main(String[] args) {
        int[] a = {1,2,3,2,2,2,5,4,2};
        System.out.println(MoreThanHalfNum_Solution(a));
    }

}

