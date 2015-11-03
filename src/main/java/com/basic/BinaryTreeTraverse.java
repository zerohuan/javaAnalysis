package com.basic;

import java.util.Stack;

/**
 * 树的前、中、后，递归、非递归遍历
 *
 * Created by yjh on 15-11-2.
 */
public class BinaryTreeTraverse {
    private static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        public TreeNode(int val) {
            this.val = val;
        }
    }

    /**
     * 前序遍历
     */
    static void preOrder(TreeNode root) {
        Stack<TreeNode> stack = new Stack<TreeNode>();
        TreeNode p = root;
        while(p != null) {
            visit(p);
            if(p.right != null)
                stack.push(p.right);
            if(p.left != null) {
                p = p.left;
            } else {
                if(stack.empty())
                    break;
                p = stack.pop();
            }
        }
    }

    static void inOrder(TreeNode root) {
        Stack<TreeNode> stack = new Stack<TreeNode>();
        TreeNode p = root;
        while(p != null || !stack.isEmpty()) {
            while (p != null) {
                stack.push(p);
                p = p.left;
            }
            if(!stack.isEmpty()) {
                p = stack.pop();
                visit(p);
                p = p.right;
            }
        }
    }

    static void visit(TreeNode node) {
        if(node != null)
            System.out.println(node.val);
    }

    static TreeNode buildTree(int[] vals) {
        TreeNode root = null;
        for(int val : vals) {
            if(root == null) {
                root = new TreeNode(val);
            } else {
                TreeNode temp = root;
                while(temp != null) {
                    if(temp.val > val) {
                        if(temp.left == null)
                            temp.left = new TreeNode(val);
                        else
                            temp = temp.left;
                    } else if(temp.val < val) {
                        if(temp.right == null)
                            temp.right = new TreeNode(val);
                        else
                            temp = temp.right;
                    } else{
                        break;
                    }
                }
            }
        }
        return root;
    }

    public static void main(String[] args) {
        int[] vals = {19,6,3,4,2,1,7,9,22};
        TreeNode root = buildTree(vals);
        preOrder(root);
        System.out.println();
        inOrder(root);
    }
}
