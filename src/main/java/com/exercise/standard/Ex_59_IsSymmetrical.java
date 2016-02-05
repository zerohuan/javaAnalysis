package com.exercise.standard;

/**
 * 对称的二叉树
 * 请实现一个函数，用来判断一颗二叉树是不是对称的。注意，如果一个二叉树同此二叉树的镜像是同样的，定义其为对称的。
 *
 * 解法：
 * 如果一个树的前序遍历序列和它的“对称前序遍历”序列相同，那么这个树是对称的。
 * 其中，对称前序遍历指的是，先遍历根节点，再遍历右子树，最后遍历左子树。
 *
 * 注意：处理子树为NULL的情况
 *
 * Created by yjh on 16-2-4.
 */
public class Ex_59_IsSymmetrical {
    public static boolean isSymmetrical(TreeNode pRoot) {
        if (pRoot == null)
            return false;
        return compareTree(pRoot, pRoot);
    }

    private static boolean compareTree(TreeNode pRoot1, TreeNode pRoot2) {
        if (pRoot1 == null)
            return pRoot2 == null;
        if (pRoot2 == null) return false;
        if (pRoot1.val != pRoot2.val) return false;
        return compareTree(pRoot1.left, pRoot2.right) &&
                compareTree(pRoot1.right, pRoot2.left);
    }

    private static class TreeNode {
        int val = 0;
        TreeNode left = null;
        TreeNode right = null;

        public TreeNode(int val) {
            this.val = val;

        }

    }

    public static void main(String[] args) {
        double d1=-0.5;
        System.out.println("Ceil d1="+Math.ceil(d1));
        System.out.println("floor d1="+Math.floor(d1));
    }
}
