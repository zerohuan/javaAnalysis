package com.exercise;

/**
 * 输入某二叉树的前序遍历和中序遍历的结果，请重建出该二叉树。
 * 假设输入的前序遍历和中序遍历的结果中都不含重复的数字。
 * 例如输入前序遍历序列{1,2,4,7,3,5,6,8}和中序遍历序列{4,7,2,1,5,3,8,6}，
 * 则重建二叉树并返回。
 *
 * 思路：
 * （1）前序遍历可以确定树和子树的根节点，比如第一个节点“1”，记住其位置preHead；
 * （2）在中序遍历序列找出上一步确定的根节点元素的位置（因为各个节点值各不相同）；
 * （3）这样可以分开左右子树，并且得到左右子树的长度
 * （4）得到左右子树长度后，在preHead基础上分情况得到左右子树的根位置
 * （5）如果同时存在左右子树：左子树的根位置=preHead+1；右子树的位置=preHead+左子树的长度；
 * （6）如果只有左子树：左子树的根位置=preHead+1；
 * （6）如果只有右子树：右子树的根位置=preHead+1；
 *
 * 注意点：
 * （1）边界条件！很关键，这一体我的思路找的很快，但是边界条件没有一开始理清楚
 * （2）尤其是只有右子树的时候，根节点的位置是preHead+1；
 *
 * Created by yjh on 15-10-26.
 */
public class RebuildBinaryTree {
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }

    public TreeNode reConstructBinaryTree(int [] pre,int [] in) {
        return reConstructBinaryTree(pre, in, 0, in.length - 1, 0);
    }

    public TreeNode reConstructBinaryTree(int [] pre,int [] in, int lowIn, int highIn, int preHead) {
        if(lowIn <= highIn) {
            TreeNode root = new TreeNode(pre[preHead]);
            if(lowIn < highIn) {
                int rootSite = search(in, pre[preHead], lowIn, highIn);
                root.left = reConstructBinaryTree(pre, in, lowIn, rootSite - 1, rootSite == lowIn ? preHead : preHead + 1);
                root.right = reConstructBinaryTree(pre, in, rootSite + 1, highIn, preHead + (rootSite == lowIn ? 1 : rootSite - lowIn + 1));
            }
            return root;
        } else
            return null;
    }

    private int search(int[] data, int key, int low, int high) {
        int mid = 0;

        for(int i = low; i <= high; i++) {
            if(data[i] == key)
                return i;
        }
        return mid;
    }

    public static void main(String[] args) {
        int[] pre = {1,2,4,7,3,5,6,8};
        int[] in = {4,7,2,1,5,3,8,6};
        RebuildBinaryTree tree = new RebuildBinaryTree();
        TreeNode root = tree.reConstructBinaryTree(pre, in);
        printTreePre(root);
    }

    public static void printTreePre(TreeNode root) {
        if(root != null) {
            System.out.print(root.val + " ");
            printTreePre(root.left);
            printTreePre(root.right);
        }
    }
}
