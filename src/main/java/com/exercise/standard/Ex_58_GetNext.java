package com.exercise.standard;

/**
 * 二叉树的下一个结点
 * 给定一个二叉树和其中的一个结点，请找出中序遍历顺序的下一个结点并且返回。
 * 注意，树中的结点不仅包含左右子结点，同时包含指向父结点的指针。
 *
 * 解法：
 * 正如下面“我的分析”，这题和我分析的一样；
 *
 * Created by yjh on 16-2-4.
 */
public class Ex_58_GetNext {
    /*
    我的分析：
    （1）右子树不为空，那么下一个节点是右子树中最左的节点；
    右子树为空，不是根节点（有父节点）：
    （2）如果它是父节点的左儿子，下一个节点就是父节点；
    （3）如果它是父节点的右儿子，继续上向，直到找到一个节点p，p是其父节点的左儿子，那么p的父节点就是下一个节点；
    （4）上滤直到根节点都没有找到节点p；
     */
    public TreeLinkNode GetNext_2(TreeLinkNode pNode) {
        if (pNode == null)
            return null;
        TreeLinkNode right = pNode.right;
        TreeLinkNode p;
        if (right != null) {
            p = right;
            while (p.left != null) {
                p = p.left;
            }
            return p;
        } else {
            TreeLinkNode parent;
            p = pNode;
            while ((parent = p.next) != null) { //p不是根节点
                if (p == parent.left)
                    return parent;
                else {
                    p = parent;
                }
            }
            return null;
        }
    }

    private static class TreeLinkNode {
        int val;
        TreeLinkNode left = null;
        TreeLinkNode right = null;
        TreeLinkNode next = null;

        TreeLinkNode(int val) {
            this.val = val;
        }
    }
}
