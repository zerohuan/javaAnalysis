package com.exercise;

import java.util.ArrayList;

/**
 * 输入一颗二叉树和一个整数，打印出二叉树中结点值的和为输入整数的所有路径。
 * 路径定义为从树的根结点开始往下一直到叶结点所经过的结点形成一条路径。
 *
 * Created by yjh on 15-10-29.
 */
public class BinaryTreePathSum {
    public ArrayList<ArrayList<Integer>> FindPath(TreeNode root,int target) {
        if(root == null) {
            return new ArrayList<ArrayList<Integer>>();
        } else {
            ArrayList<ArrayList<Integer>> left = FindPath(root.left, target - root.val);
            ArrayList<ArrayList<Integer>> right = FindPath(root.right, target - root.val);
            if(root.left != null && root.right != null) {
                left.addAll(right);
                addVal(root.val, left);
                return left;
            } else if(root.left != null) {
                addVal(root.val, left);
                return left;
            } if(root.right != null) {
                addVal(root.val, right);
                return right;
            } else {
                if(root.val != target) {
                    return left;
                } else {
                    ArrayList<Integer> res = new ArrayList<Integer>();
                    res.add(root.val);
                    left.add(res);
                    return left;
                }
            }
        }

    }

    public void addVal(int val, ArrayList<ArrayList<Integer>> list) {
        for(ArrayList<Integer> l : list) {
            l.add(0, val);
        }
    }

    static class TreeNode {
        int val = 0;
        TreeNode left = null;
        TreeNode right = null;

        public TreeNode(int val) {
            this.val = val;

        }

    }
}
