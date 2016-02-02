package com.exercise.standard;

/**
 * 输入一棵二叉树，求该树的深度。从根结点到叶结点依次经过的结点（含根、叶结点）形成树的一条路径，
 * 最长路径的长度为树的深度。
 *
 * 解法：
 * 分治法，时间：O(lgn)
 *
 * Created by yjh on 16-2-2.
 */
public class Ex_39_1_TreeDepth {
    /*
    我的解法：
    分治法，一个树的深度 = Max(Depth(左子树), Depth(右子树)) + 1
     */
    public static int TreeDepth(TreeNode root) {
        if (root == null)
            return 0;
        return Math.max(TreeDepth(root.left), TreeDepth(root.right)) + 1;
    }

    public static void main(String[] args) {
    }

    private static class TreeNode {
        int val = 0;
        TreeNode left = null;
        TreeNode right = null;

        public TreeNode(int val) {
            this.val = val;

        }

    }
}
