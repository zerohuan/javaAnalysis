package com.basic.collection;

import java.util.Stack;

/**
 * Created by yjh on 15-12-14.
 */
public class TreeUtils {
    /**
     * 前序遍历，迭代
     * @param tree 二叉树引用
     */
    static void printTreeNode(BinaryTree tree) {
        if(tree == null || tree.root() == null)
            return;
        Stack<BinaryTree.TreeNode> stack = new Stack<>();
        BinaryTree.TreeNode p = tree.root();
        while(p != null) {
            System.out.print(p);
            if(p.right != null) {
                stack.push(p.right);
            }
            if(p.left != null)
                p = p.left;
            else {
                if (stack.isEmpty())
                    break;
                p = stack.pop();
            }
        }
    }
}
