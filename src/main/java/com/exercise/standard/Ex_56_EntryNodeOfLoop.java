package com.exercise.standard;

/**
 * 链表中环的入口结点
 * 一个链表中包含环，请找出该链表的环的入口结点。
 *
 * 解法：
 *
 * 注意：
 * 先判断是否有环，再再找出环
 *
 * Created by yjh on 16-2-3.
 */
public class Ex_56_EntryNodeOfLoop {
    public static ListNode EntryNodeOfLoop(ListNode pHead) {
        if (pHead == null)
            return null;
        ListNode p = pHead, q = p;
        boolean isLoop = false;
        while (q != null && q.next != null) {
            p = p.next;
            q = q.next.next;
            if (p == q) {
                isLoop = true;
                break;
            }
        }
        if (isLoop) {
            ListNode t = pHead;
            while (t != p) {
                t = t.next;
                p = p.next;
            }
            return t;
        } else
            return null;
    }

    private static class ListNode {
        int val;
        ListNode next = null;

        ListNode(int val) {
            this.val = val;
        }
    }
}
