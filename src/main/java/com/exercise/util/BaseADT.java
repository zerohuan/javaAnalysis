package com.exercise.util;

/**
 * Created by yjh on 15-12-11.
 */
public class BaseADT {
    public static class TreeNode {
        public int value;
        public TreeNode left;
        public TreeNode right;

        public TreeNode(int value) {
            this.value = value;
        }

    }

    public static TreeNode buildBinarySearchTree(int[] preOrder) {
        if(preOrder == null || preOrder.length == 0)
            return null;

        TreeNode root = null;

        for(int num : preOrder) {
            root = insertBST(root, num);
        }

        return root;
    }

    public static TreeNode insertBST(TreeNode root, int num) {
        if(root == null) {
            return new TreeNode(num);
        } else {
            if(root.value < num) {
                root.right = insertBST(root.right, num);
            } else if(root.value > num) {
                root.left = insertBST(root.left, num);
            }
            return root;
        }
    }

    public static void printBinaryTree(TreeNode root) {
        if(root != null) {
            System.out.print(root.value);
            System.out.print("\t");
            printBinaryTree(root.left);
            printBinaryTree(root.right);
        }
    }



    public static void main(String[] args) {
        printBinaryTree(buildBinarySearchTree(new int[]{7,4,9,2,5,0,1}));
    }
}
