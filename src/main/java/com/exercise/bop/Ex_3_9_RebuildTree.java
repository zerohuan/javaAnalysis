package com.exercise.bop;

/**
 * ÖØ½¨¶þ²æÊ÷
 * Created by yjh on 2016/3/13.
 */
public class Ex_3_9_RebuildTree {

    public static Node rebuild(int[] prevOrder, int[] inOrder) {
        if (prevOrder != null && inOrder != null && prevOrder.length == inOrder.length)
            return reBuild(prevOrder, 0, inOrder, 0, inOrder.length - 1);
        return null;
    }

    private static Node reBuild(int[] prevOrder, int rootIndex, int[] inOrder, int li, int ri) {
        if (li < ri) {
            int root = prevOrder[rootIndex];
            int i;
            for (i = li; i <= ri; ++i) {
                if (inOrder[i] == root)
                    break;
            }
            Node rootN = new Node(root);
            int leftLen = i - li;
            int rightLen = ri - i;
            if (rightLen > 0)
                rootN.right = reBuild(prevOrder, rootIndex + leftLen + 1, inOrder, i + 1, ri);
            if (leftLen > 0)
                rootN.left = reBuild(prevOrder, rootIndex + 1, inOrder, li, i - 1);
            return rootN;
        } else
            return new Node(prevOrder[rootIndex]);
    }

    private static void printTree(Node root) {
        if (root != null) {
            System.out.println(root.val);
            printTree(root.left);
            printTree(root.right);
        }
    }

    public static void main(String[] args) {
        Node root = rebuild(new int[]{1,2,4,3,5,6}, new int[]{4,2,1,5,3,6});
        printTree(root);
    }

    private static class Node {
        private int val;
        private Node left;
        private Node right;

        public Node(int val) {
            this.val = val;
        }
    }
}
