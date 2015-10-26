package com.exercise;

import java.util.ArrayList;

/**
 * 输入一个链表，从尾到头打印链表每个节点的值。
 *
 * 思路：
 * 递归，太简单。
 *
 * Created by yjh on 15-10-25.
 */
public class PrintList {

    public static ArrayList<Integer> printListFromTailToHead(ListNode listNode) {
        if(listNode == null) {
            return new ArrayList<Integer>();
        } else {
            ArrayList<Integer> values = printListFromTailToHead(listNode.next);
            values.add(listNode.val);
            return values;
        }
    }

    static class ListNode {
        int val;
        ListNode next = null;

        ListNode(int val) {
            this.val = val;
        }
    }

    public static void main(String[] args) {
        ListNode head, listNode;
        head = listNode = new ListNode(5);
        listNode = listNode.next = new ListNode(4);
        listNode.next = new ListNode(3);
        System.out.println(printListFromTailToHead(head));
    }
}
