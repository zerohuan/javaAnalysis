package com.exercise.standard;

/**
 * 输入一棵二叉树，判断该二叉树是否是平衡二叉树。
 *
 * 解法：
 * 基于上一题很容易能够想出一个递归的可行解法，但是怎么样避免重复计算是一个重要的问题。
 * 要想在一个递归遍历的过程中解决这个问题，显然每次判断子树高度的时候要同时返回是否是平衡的和深度两个返回结果，
 * 这里用一个Data类来实现。
 *
 * Created by yjh on 16-2-2.
 */
public class Ex_39_2_IsBalanced {
    public boolean IsBalanced_Solution(TreeNode root) {
        if (root == null)
            return true;
        return IsBalanced(root).isBalanced;
    }

    private static Data IsBalanced(TreeNode root) {
        if (root == null)
            return new Data(0, true);
        Data left = IsBalanced(root.left);
        Data right = IsBalanced(root.right);

        return new Data(Math.max(left.depth, right.depth) + 1,
                left.isBalanced && right.isBalanced && Math.abs(left.depth - right.depth) <= 1);
    }
    private static class Data {
        private boolean isBalanced;
        private int depth;

        public Data(int depth, boolean isBalanced) {
            this.depth = depth;
            this.isBalanced = isBalanced;
        }
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
