package com.exercise;

import java.util.ArrayList;

/**
 *
 * Created by yjh on 15-11-11.
 */
public class MirrorBinary {
    static class TreeNode {
        int value;
        TreeNode left;
        TreeNode right;

        public TreeNode(int value) {
            this.value = value;
        }
    }

    private static void mirrorRecursively(TreeNode root) {
        if(root == null || root.left == root.right)
            return;

        ArrayList<TreeNode> queue = new ArrayList<>();
        queue.add(root);
        while(!queue.isEmpty()) {
            TreeNode node = queue.remove(0);
            TreeNode temp = node.left;
            node.left = node.right;
            node.right = temp;
            if(node.left != null)
                queue.add(node.left);
            if(node.right != null)
                queue.add(node.right);
        }
    }
}
