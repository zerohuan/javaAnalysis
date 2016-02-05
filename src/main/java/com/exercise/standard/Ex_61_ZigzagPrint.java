package com.exercise.standard;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * 按之字形顺序打印二叉树
 * 请实现一个函数按照之字形打印二叉树，即第一行按照从左到右的顺序打印，
 * 第二层按照从右至左的顺序打印，第三行按照从左到右的顺序打印，其他行以此类推。
 *
 * 解法：
 * 真好，解法思想和我的分析一致
 *
 * Created by yjh on 16-2-4.
 */
public class Ex_61_ZigzagPrint {
    /*
    我的分析：
    和按行相比，这里可以用两个栈；
    首先将根节点放入stack1，从stack1中读取的节点，将它的儿子节点按照先左后右的顺序放入stack2；
    stack1读取完，读取stack2中的节点，stack2中读取的节点，将它的儿子节点按照先右后左的顺序放入stack1；
    这样就可以实现“之”字形打印。
     */
    public ArrayList<ArrayList<Integer>> Print(TreeNode pRoot) {
        ArrayList<ArrayList<Integer>> res = new ArrayList<>();
        if (pRoot != null) {
            //栈1
            LinkedList<TreeNode> stack1 = new LinkedList<>();
            //栈2
            LinkedList<TreeNode> stack2 = new LinkedList<>();
            stack1.offer(pRoot);
            while (!stack1.isEmpty() || !stack2.isEmpty()) {
                ArrayList<Integer> list = new ArrayList<>();
                if (!stack1.isEmpty()) {
                    while (!stack1.isEmpty()) {
                        TreeNode node = stack1.pop();
                        list.add(node.val);
                        if (node.left != null)
                            stack2.push(node.left);
                        if (node.right != null)
                            stack2.push(node.right);
                    }
                } else {
                    while (!stack2.isEmpty()) {
                        TreeNode node = stack2.pop();
                        list.add(node.val);
                        if (node.right != null)
                            stack1.push(node.right);
                        if (node.left != null)
                            stack1.push(node.left);
                    }
                }
                res.add(list);
            }
        }
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
