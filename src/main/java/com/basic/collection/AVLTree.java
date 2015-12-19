package com.basic.collection;

/**
 * 平衡二叉平衡树
 * 不包括重复的元素
 * Created by yjh on 15-12-13.
 */
public class AVLTree<E extends Comparable<? super E>> extends BinaryTree<E> {
    private AVLTreeNode<E> root;

    /**
     * 插入一个新节点，并调整维持平衡状态
     * @param e
     * @param t
     * @return
     */
    public AVLTreeNode<E> insert(E e, AVLTreeNode<E> t) {
        if(e == null)
            throw new NullPointerException();
        if(t == null) {
            t = new AVLTreeNode<>(e);
        } else {
            E xe;
            if((xe = t.value) == e || xe.equals(e)) //元素相等，忽略
                return t;
            else if(xe.compareTo(e) > 0) { //插入左子树
                final AVLTreeNode<E> l = t.left = insert(e, t.left);
                if(height(l) - height(t.right) == 2) {
                    if(l.value.compareTo(e) > 0) //在左儿子的左子树中插入，左双旋
                        t = singleRotateWithLeft(t);
                    else
                        t = doubleRotateWithLeft(t);
                }
            } else { //插入右子树
                final AVLTreeNode<E> r = t.right = insert(e, t.right);
                if(height(r) - height(t.left) == 2) {
                    if(r.value.compareTo(e) < 0) //右儿子的右子树
                        t = singleRotateWithRight(t);
                    else
                        t = doubleRotateWithRight(t);
                }
            }
        }
        computeHeight(t);
        return t;
    }

    /**
     * 删除指定元素值的节点
     * @param e
     * @param t
     * @return
     */
    public AVLTreeNode<E> delete(E e, AVLTreeNode<E> t) {
        if(e == null)
            throw new NullPointerException();
        E et;
        if(t != null) {
            //在左子树中删除
            if((et = t.value).compareTo(e) > 0) {
                final AVLTreeNode<E> l = t.left = delete(e, t.left);
                final AVLTreeNode<E> r;
                if(height(r = t.right) - height(l) == 2) {
                    if(height(r.right) - height(r.left) < 0)
                        t.right = doubleRotateWithRight(r);
                    else //l的左右子树高度相同可以使用右-单旋转
                        t.right = singleRotateWithRight(r);
                }
            }
            //在右子树中删除
            else if(et.compareTo(e) < 0) {
                final AVLTreeNode<E> r = t.right = delete(e, t.right);
                final AVLTreeNode<E> l;
                if(height(l = t.left) - height(r) == 2) {
                    if(height(l.left) - height(l.right) < 0)
                        t.left = doubleRotateWithLeft(l);
                    else
                        t.left = singleRotateWithLeft(l);
                }
            }
            //删除该节点
            else {
                if(t.right != null && t.left != null) {
                    AVLTreeNode<E> p = t.right; final E v; final AVLTreeNode<E> l;
                    while(p.left != null)
                        p = p.left;
                    v = t.value = p.value;
                    t.right = delete(v, t.right);
                    if (height(l = t.left) - height(t.right) == 2) {
                        if(height(l.left) - height(l.right) < 0)
                            t.left = doubleRotateWithLeft(l);
                        else
                            t.left = singleRotateWithLeft(l);
                    }
                }
                else {
                    AVLTreeNode<E> p = null;
                    //节点最多有1个儿子
                    if(t.right != null) {
                        p = t.right;
                        t.right = null;
                    } else if(t.left != null) {
                        p = t.left;
                        t.left = null;
                    }
                    t = p;
                }
            }
        }
        return t;
    }

    /*
    BinaryTree实现
     */
    @Override
    public void insert(E e) {
        root = insert(e, root);
    }

    @Override
    public void delete(E e) {
        root = delete(e, root);
    }

    @Override
    public TreeNode root() {
        return root;
    }

    /*
    旋转方法
     */
    //左单旋
    AVLTreeNode<E> singleRotateWithLeft(AVLTreeNode<E> k2) {
        AVLTreeNode<E> k1 = k2.left;
        k2.left = k1.right;
        k1.right = k2;

        //重新计算高度
        computeHeight(k2);
        computeHeight(k1);

        return k1;
    }

    //右单旋
    AVLTreeNode<E> singleRotateWithRight(AVLTreeNode<E> k2) {
        AVLTreeNode<E> k1 = k2.right;
        k2.right = k1.left;
        k1.left = k2;

        computeHeight(k2);
        computeHeight(k1);

        return k1;
    }

    //左双旋转：先右单旋再左单旋
    AVLTreeNode<E> doubleRotateWithLeft(AVLTreeNode<E> k3) {
        k3.left = singleRotateWithRight(k3.left);
        return singleRotateWithLeft(k3);
    }

    //右双旋转：先左单旋再右单旋
    AVLTreeNode<E> doubleRotateWithRight(AVLTreeNode<E> k3) {
        k3.right = singleRotateWithLeft(k3.right);
        return singleRotateWithRight(k3);
    }

    //计算一个节点的高度
    static int computeHeight(AVLTreeNode<?> node) {
        AVLTreeNode<?> r, l;
        return node.height = Math.max((l = node.left) == null ? -1 : l.height,
                (r = node.right) == null ? -1 : r.height) + 1;
    }

    static int height(AVLTreeNode<?> node) {
        return node == null ? -1 : node.height;
    }

    /*
        节点类
         */
    static class AVLTreeNode<E> extends BinaryTree.TreeNode<E, AVLTreeNode<E>> {
        int height;

        public AVLTreeNode(E value) {
            super(value);
        }

        @Override
        public String toString() {
            return "[" +
                    "height=" + height +
                    ", value=" + value +
                    ']';
        }
    }


    public static void main(String[] args) {
        AVLTree<Integer> tree = new AVLTree<>();
        tree.insert(5);
        tree.insert(8);
        tree.insert(1);
        tree.insert(4);
        tree.insert(3);
        tree.insert(9);
        tree.delete(5);
        TreeUtils.printTreeNode(tree);
    }
}
