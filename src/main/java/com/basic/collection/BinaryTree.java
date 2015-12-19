package com.basic.collection;

/**
 * 基本二叉树
 * Created by yjh on 15-12-13.
 */
public class BinaryTree<E> {
    private TreeNode root;

    public void insert(E e) {

    }

    public void delete(E e) {

    }

    public TreeNode root() {
        return root;
    }

    /**
     * 包中所有二叉树类的节点
     * @param <E>
     */
    public static class TreeNode<E,N extends TreeNode> {
        E value;
        N left;
        N right;

        public TreeNode(E value) {
            this.value = value;
        }
    }
}
