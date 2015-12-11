package com.exercise.standard;

import java.util.ArrayList;

/**
 * 复杂链表复制
 *
 * Created by yjh on 15-12-3.
 */
public class Ex_26_ComplexLinkedListClone {
    private static class RandomListNode {
        private int label;
        private RandomListNode next;
        private RandomListNode random;

        public RandomListNode(int val) {
            this.label = val;
        }

        @Override
        public String toString() {
            return "RandomListNode{" +
                    "label=" + label +
                    ", random=" + (random != null ? random.label : -1) +
                    '}';
        }
    }

    //复制节点
    private static void cloneNodes(RandomListNode head) {
        RandomListNode p = head;
        while(p != null) {
            RandomListNode pClone = new RandomListNode(p.label);
            pClone.next = p.next;
            p.next= pClone;
            p = pClone.next;
        }
    }

    //复制指向关系
    private static void connectSiblingNodes(RandomListNode head) {
        RandomListNode p = head;
        while(p != null) {
            RandomListNode pClone = p.next;
            if(p.random != null) {
                pClone.random = p.random.next;
            }
            p = pClone.next;
        }
    }

    //分离两个链表
    private static RandomListNode reconnectNodes(RandomListNode head) {
        RandomListNode p = head;
        RandomListNode n = null, headClone = null;
        if(p != null) {
            headClone = n = p.next;
            p.next = n.next;
            p = p.next;
        }

        while(p != null) {
            n.next = p.next;
            n = n.next;
            p.next = n.next;
            p = p.next;
        }
        return headClone;
    }

    private static RandomListNode cloneComplexList(RandomListNode head) {
        if(head == null) return null;
        cloneNodes(head);
        connectSiblingNodes(head);
        return reconnectNodes(head);
    }

    private static void printComplexList(RandomListNode head) {
        RandomListNode p = head;
        while(p != null) {
            System.out.print(p + " ");
            p = p.next;
        }
        System.out.println();
    }

    private static RandomListNode createdComplexList(int[] order, int[] siblingOrder) {
        int len;
        if(order == null || siblingOrder == null || (len = order.length) == 0 || len != siblingOrder.length)
            return null;

        RandomListNode head = new RandomListNode(order[0]), p = head;
        ArrayList<RandomListNode> list = new ArrayList<>();
        list.add(head);
        for(int i = 1; i < len; ++i) {
            RandomListNode next = new RandomListNode(order[i]);
            list.add(next);
            p.next = next;
            p = next;
        }
        for(int i = 0; i < len; ++i) {
            int s = siblingOrder[i];
            if(s != -1)
                list.get(i).random = list.get(s);
        }
        return head;
    }

    public static void main(String[] args) {
        RandomListNode head = createdComplexList(new int[]{6,3,8,9,1,5,4}, new int[]{-1,0,5,-1,2,1,3});
        printComplexList(head);
        printComplexList(cloneComplexList(head));
        printComplexList(head);
    }
}
