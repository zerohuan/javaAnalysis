package com.exercise;

/**
 * 输入两颗二叉树A，B，判断B是不是A的子结构。
 *
 * Created by yjh on 15-10-28.
 */
public class HasSubTree {
    static class TreeNode {
        int val = 0;
        TreeNode left = null;
        TreeNode right = null;

        public TreeNode(int val) {
            this.val = val;

        }

    }

    public static boolean HasSubtree(TreeNode root1,TreeNode root2) {
        boolean result = false;

        if(root1 != null && root2 != null) {
            if(root1.val == root2.val) {
                result = doHasSubtree(root1.left, root2.left)
                        && doHasSubtree(root1.right, root2.right);
            }
            if(!result) {
                result = HasSubtree(root1.left, root2);
            }
            if(!result) {
                result = HasSubtree(root1.right, root2);
            }
        }

        return result;
    }

    public static boolean doHasSubtree(TreeNode root1, TreeNode root2) {
        if(root2 == null)
            return true;
        if(root1 == null)
            return false;

        if(root1.val == root2.val) {
            return HasSubtree(root1.left,root2.left) && HasSubtree(root1.right,root2.right) ;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        TreeNode r1 = new TreeNode(8);
        r1.left = new TreeNode(8);
        r1.right = new TreeNode(7);
        r1.left.left = new TreeNode(9);
        r1.left.right = new TreeNode(2);
        r1.left.right.left = new TreeNode(4);
        r1.left.right.right = new TreeNode(7);

        TreeNode r2 = new TreeNode(8);
        r2.left = new TreeNode(9);
        r2.right = new TreeNode(2);

        System.out.println(HasSubtree(r1, r2));
    }
}
