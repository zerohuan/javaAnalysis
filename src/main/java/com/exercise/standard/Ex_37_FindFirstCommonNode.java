package com.exercise.standard;

/**
 * 输入两个链表，找出它们的第一个公共结点。
 *
 * 解法：
 * 先遍历求得两个链表的长度，长的链表先向后遍历直到剩下的长度和短链表相等后，两个链表同时开始遍历，每遍历一个就比较是否相等，
 * 节点相等则返回。
 * 时间：O(m+n);不需要辅助空间；
 *
 * 相关问题：树中的最近公共祖先节点
 * （1）在二叉树中：
 *
 * （2）在节点有parent指针的树中：
 *
 * （3）在节点没有parent指针的树中：
 *
 * Created by yjh on 16-2-2.
 */
public class Ex_37_FindFirstCommonNode {
    public ListNode FindFirstCommonNode(ListNode pHead1, ListNode pHead2) {
        if (pHead1 == null || pHead2 == null)
            return null;
        ListNode p1 = pHead1, p2 = pHead2;
        int p1Len = getListLength(pHead1);
        int p2Len = getListLength(pHead2);
        if (p1Len > p2Len) {
            p1 = pHead2;
            p2 = pHead1;
        }
        for (int i = 0; i < Math.abs(p1Len - p2Len); ++i) {
            p2 = p2.next;
        }
        while (p1 != null && p2 != null) {
            if (p1 == p2)
                return p1;
            else {
                p1 = p1.next;
                p2 = p2.next;
            }
        }
        return null;
    }
    private static int getListLength(ListNode list) {
        int len = 0;
        while (list != null) {
            len++;
            list = list.next;
        }
        return len;
    }

    private static class ListNode {
        int val;
        ListNode next = null;

        ListNode(int val) {
            this.val = val;
        }
    }
}
