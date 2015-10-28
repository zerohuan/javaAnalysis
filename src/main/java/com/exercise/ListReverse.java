package com.exercise;

/**
 * 反转链表
 *
 * 思路：用一个指针遍历，借助一个temp指针保存next，再重定向current的next为prev；
 *
 * Created by yjh on 15-10-28.
 */
public class ListReverse {
    public ListNode ReverseList(ListNode head) {
        ListNode p = null;
        while(head != null) {
            ListNode temp = head.next;
            head.next = p;
            p = head;
            head = temp;
        }
        return p;
    }

    static class ListNode {
        int val;
        ListNode next = null;

        ListNode(int val) {
            this.val = val;
        }
    }
}
