package com.exercise;

/**
 * Created by yjh on 15-10-28.
 */
public class ListTailK {
    public static ListNode FindKthToTail(ListNode head,int k) {
        ListNode temp = head;
        int i = 1;

        if(head == null || k == 0)
            return null;

        while(head.next != null) {
            head = head.next;
            if(++i > k) {
                temp=temp.next;
            }
        }

        return i >= k ? temp : null;
    }

    static class ListNode {
        int val;
        ListNode next = null;

        ListNode(int val) {
            this.val = val;
        }
    }

    public static void print(ListNode node) {
        while(node != null) {
            System.out.print(node.val + " ");
            node = node.next;
        }
    }

    public static ListNode Merge(ListNode list1,ListNode list2) {
        ListNode list = null;
        ListNode head = null;
        while(list1 != null && list2 != null) {
            if(list1.val < list2.val) {
                if(list == null) {
                    head = list = list1;
                } else {
                    list.next = list1;
                    list = list.next;
                }
                list1 = list1.next;
            } else {
                if(list == null) {
                    head = list = list2;
                } else {
                    list.next = list2;
                    list = list.next;
                }
                list2 = list2.next;
            }
        }

        if(list1 != null) {
            if (list != null)
                list.next = list1;
            else
                head = list1;
        } else if(list2 != null) {
            if (list != null)
                list.next = list2;
            else
                head = list2;
        }

        return head;
    }

    public static void main(String[] args) {
        ListNode node = new ListNode(1);ListNode listNode = node;
        node.next = new ListNode(2);node = node.next;
        node.next = new ListNode(3);node = node.next;
        node.next = new ListNode(4);node = node.next;
        node.next = new ListNode(5);node = node.next;
        node.next = new ListNode(6);

        ListNode node1 = new ListNode(1);ListNode listNode2 = node1;
        node1.next = new ListNode(2);node1 = node1.next;
        node1.next = new ListNode(3);node1 = node1.next;
        node1.next = new ListNode(4);node1 = node1.next;
        node1.next = new ListNode(5);node1 = node1.next;
        node1.next = new ListNode(6);

        print(Merge(listNode, listNode2));
    }
}
