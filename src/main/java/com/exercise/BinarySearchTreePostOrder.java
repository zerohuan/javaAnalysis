package com.exercise;

/**
 * 输入一个整数数组，判断该数组是不是某二叉搜索树的后序遍历的结果。
 * 如果是则输出Yes,否则输出No。假设输入的数组的任意两个数字都互不相同。
 *
 * 思路：
 * 根据搜索二叉树的特点：一个节点的值比左子树的值都都大，比右子树的都小，那么后序遍历中，
 * 输出一个节点前一定是先遍历完比它小的左子树，再遍历完比它大的数；
 *
 * Created by yjh on 15-10-29.
 */
public class BinarySearchTreePostOrder {
    //最佳做法
    public static boolean VerifySquenceOfBST(int [] sequence) {
        int len = sequence.length;
        if(len == 0)
            return false;
        int i;
        while(--len > 0) {
            i = 0;
            while(sequence[i] < sequence[len]) i++;
            while(sequence[i] > sequence[len]) i++;
            if(i < len)
                return false;
        }


        return true;
    }

    public static boolean VerifySquenceOfBST1(int [] sequence) {
        if(sequence == null || sequence.length == 0)
            return false;
        if(sequence.length <= 2)
            return true;
        TreeNode root = new TreeNode(sequence[sequence.length - 1]);
        for(int i = sequence.length - 2; i >= 0; i--) {
            TreeNode temp = root;
            while(temp != null) {
                if(temp.val > sequence[i]) {
                    if(temp.left != null)
                        temp = temp.left;
                    else {
                        temp.left = new TreeNode(sequence[i]);
                        temp = null;
                    }
                }
                else if(temp.val < sequence[i]) {
                    if(temp.left != null)
                        return false;
                    if(temp.right != null) {
                        temp = temp.right;
                    } else {
                        temp.right = new TreeNode(sequence[i]);
                        temp = null;
                    }
                }
            }
        }
        return true;
    }

    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        public TreeNode(int val) {
            this.val = val;
        }
    }

    public static void main(String[] args) {
        int[] a = {7,4,6,5};
        System.out.println(VerifySquenceOfBST(a));
    }
}
