package com.exercise.standard;

/**
 * 删除链表中重复的结点
 * 在一个排序的链表中，存在重复的结点，请删除该链表中重复的结点，重复的结点不保留，返回链表头指针。
 * 例如，链表1->2->3->3->4->4->5 处理后为 1->2->5
 *
 *
 * Created by yjh on 16-2-3.
 */
public class Ex_57_DeleteDuplication {
    public static ListNode deleteDuplication(ListNode pHead) {
        if (pHead == null)
            return null;
        ListNode p = pHead, tempH = new ListNode(-1), prev = tempH;
        tempH.next = pHead;
        while (p != null) {
            ListNode next = p.next;
            while (next != null && next.val == p.val) {
                next = next.next;
            }
            if (p.next != next) {
                p.next = null;
                prev.next = next;
                p = prev;
            }
            prev = p;
            p = next;
        }
        pHead = tempH.next;
        tempH.next = null;
        return pHead;
    }

    private static class ListNode {
        int val;
        ListNode next = null;

        ListNode(int val) {
            this.val = val;
        }

        public ListNode setNext(ListNode next) {
            this.next = next;
            return next;
        }
    }

    private static void print(ListNode l) {
        if (l != null) {
            System.out.println(l.val);
            print(l.next);
        }
    }

    public static void main(String[] args) {
        ListNode p = new ListNode(1);
        p.setNext(new ListNode(2)).setNext(new ListNode(3)).setNext(new ListNode(3))
                .setNext(new ListNode(4)).setNext(new ListNode(4)).setNext(new ListNode(5));
        print(deleteDuplication(p));
    }
}
