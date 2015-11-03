package com.exercise;

/**
 * 输入一棵二叉搜索树，将该二叉搜索树转换成一个排序的双向链表。
 * 要求不能创建任何新的结点，只能调整树中结点指针的指向。
 *
 *
 *
 * Created by yjh on 15-11-2.
 */
public class BinaryTree2DoublyLinkedList {
    static class TreeNode {
        int val = 0;
        TreeNode left = null;
        TreeNode right = null;

        public TreeNode(int val) {
            this.val = val;

        }
    }

    public static TreeNode Convert(TreeNode pRootOfTree) {
        if(pRootOfTree == null)
            return null;
        if(pRootOfTree.left == null && pRootOfTree.right == null) {
            return pRootOfTree;
        }
        TreeNode left = Convert(pRootOfTree.left);
        TreeNode right = Convert(pRootOfTree.right);
        TreeNode p = left;
        if(left != null) {
            while(p.right != null) {
                p = p.right;
            }
            p.right = pRootOfTree;
            pRootOfTree.left = p;
        }
        if(right != null) {
            pRootOfTree.right = right;
            right.left = pRootOfTree;
        }


        return left == null ? pRootOfTree : left;
    }

    public static void main(String[] args) {
        //测试3个节点
        TreeNode root = new TreeNode(2);
        root.left = new TreeNode(1);
        root.right = new TreeNode(3);
        TreeNode rest = Convert(root);
        System.out.println(rest.val);
        System.out.println(rest.right.left.val);
        System.out.println(rest.right.right.left.val);

        //测试2个节点
        TreeNode root2 = new TreeNode(2);
        root2.left = new TreeNode(1);
        TreeNode rest2 = Convert(root2);
        System.out.println(rest2.val);
        System.out.println(rest2.right.right);

        //测试null
        System.out.println(Convert(null));
    }
}
