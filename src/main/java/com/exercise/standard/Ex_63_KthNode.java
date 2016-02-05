package com.exercise.standard;

/**
 * 二叉搜索树的第k个结点
 * 给定一颗二叉搜索树，请找出其中的第k大的结点。例如， 5 / \ 3 7 /\ /\ 2 4 6 8 中，
 * 按结点数值大小顺序第三个结点的值为4。
 *
 * Created by yjh on 16-2-4.
 */
public class Ex_63_KthNode {
    public TreeNode KthNode(TreeNode pRoot, int k) {
        if (pRoot == null || k == 0)
            return null;
        this.k = k;
        return kthNode(pRoot);
    }
    private int k;
    private TreeNode kthNode(TreeNode root) {
        TreeNode res = null;

        if (root.left != null)
            res = kthNode(root.left);
        if (res == null) {
            if (k == 1)
                res = root;
            --k;
        }
        if (res == null && root.right != null)
            res = kthNode(root.right);

        return res;
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
