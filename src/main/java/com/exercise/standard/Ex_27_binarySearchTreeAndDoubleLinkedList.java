package com.exercise.standard;


import com.exercise.util.BaseADT;
import com.exercise.util.BaseADT.TreeNode;

import java.util.Stack;

/**
 * 输入一棵二叉搜索树，将该二叉搜索树转换成一个排序的双向链表。要求不能创建任何新的结点，只能调整树中结点指针的指向。
 * Created by yjh on 15-12-11.
 */
public class Ex_27_binarySearchTreeAndDoubleLinkedList {

    /*
    迭代算法，实际就是树的中序遍历
     */
    private static TreeNode convert(TreeNode root) {
        if(root == null)
            return null;
        Stack<TreeNode> stack = new Stack<>();
        TreeNode prev = null, head = null;
        TreeNode p = root;

        while(p != null || !stack.isEmpty()) {
            while(p != null) {
                stack.push(p);
                p = p.left;
            }
            if(!stack.isEmpty()) {
                p = stack.pop();
                if(prev != null) {
                    prev.right = p;
                    p.left = prev;
                } else
                    head = p;
                prev = p;
                p = p.right;
            }
        }
        return head;
    }

    /*
    递归算法，中序遍历
     */
    private static TreeNode end = null;
    private static TreeNode Convert(TreeNode root) {
        if(root == null)
            return null;

        if(root.left == null && root.right == null) {
            end = root;
            return root;
        }

        TreeNode leftList = Convert(root.left);
        if(leftList != null) {
            end.right = root;
            root.left = end;
        }
        end = root;
        TreeNode rightList = Convert(root.right);
        if(rightList != null) {
            root.right = rightList;
            rightList.left = root;
        }

        return (leftList != null) ? leftList : root;
    }

    private static void printLinkedList(TreeNode head) {
        while(head != null) {
            System.out.print(head.value);
            System.out.print("\t");
            head = head.right;
        }
    }

    public static void main(String[] args) {
        TreeNode root = BaseADT.buildBinarySearchTree(new int[]{7, 4, 9, 2, 5, 0, 1});
        if (root != null) {
            TreeNode head = convert(root);
            printLinkedList(head);
        }
    }
}
