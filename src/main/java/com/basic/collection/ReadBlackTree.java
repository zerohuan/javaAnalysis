package com.basic.collection;

/**
 * 红黑树实现
 * Created by yjh on 15-12-14.
 */
public class ReadBlackTree<E extends Comparable<? super E>> extends BinaryTree<E> {
    private RBTreeNode<E> root;

    @Override
    public void insert(E e) {
        if(e == null)
            throw new NullPointerException();
        RBTreeNode<E> p = root, prev = null; E v;
        while(p != null) {
            prev = p;
            if((v = p.value) == e || v.equals(e)) {
                return;
            }
            else if(v.compareTo(e) > 0) {
                p = p.left;
            } else {
                p = p.right;
            }
        }
        RBTreeNode<E> newNode = new RBTreeNode<>(e);
        newNode.parent = prev;
        if(prev == null)
            root = newNode;
        else if(prev.value.compareTo(e) > 0)
            prev.left = newNode;
        else
            prev.right = newNode;
        newNode.red = true;
        root = rbInsertFixUp(root, newNode);
    }

    @Override
    public void delete(E e) {
        if(e == null)
            throw new NullPointerException();
        RBTreeNode<E> p = root; E v;
        //查找应该删除的节点
        while(p != null) {
            if(e == (v = p.value) || e.equals(v))
                break;
            else if(e.compareTo(v) > 0)
                p = p.left;
            else
                p = p.right;
        }
        //找到了要删除的节点
        if(p != null) {
            //左右儿子不为空，找一个替代节点交换值
            if(p.left != null && p.right != null) {
                RBTreeNode<E> s = successor(p); //找到右子树中最小元素节点代替p
                p.value = s.value;
                p = s;
            }

            RBTreeNode<E> replacement = (p.left != null ? p.left : p.right);
            if(replacement != null) {
                replacement.parent = p.parent;
                if(p.parent == null)
                    root = replacement;
                else if(p == p.parent.left)
                    p.parent.left = replacement;
                else
                    p.parent.right = replacement;

                p.parent = p.left = p.right = null; //for GC

                if(!p.red)
                    root = rbDeleteFixUp(root, replacement);
            } else if(p.parent == null) {
                root = null;
            } else { //p没有儿子节点，如果它是黑色，要进行修补
                if(!p.red)
                    root = rbDeleteFixUp(root, p);
                //分离节点p
                if(p.parent == null)
                    root = p;
                else {
                    if(p.parent.left == p)
                        p.parent.left = null;
                    else
                        p.parent.right = null;
                    p.parent = null;
                }
            }

        }
    }

    @Override
    public TreeNode root() {
        return root;
    }

    /*
    子树操作
     */
    //移动子树
    static <E extends Comparable<? super E>>
    void rbTransplant(ReadBlackTree<E> t, RBTreeNode<E> u, RBTreeNode<E> v) {
        RBTreeNode<E> up, ul, ur;
        if((up = u.parent) == null)
            t.root = v;
        else if(u == up.left)
            up.left = v;
        else
            up.right = v;
        if(v != null)
            v.parent = up;
    }

    //寻找后续节点
    RBTreeNode<E> successor(RBTreeNode<E> t) {
        if (t == null)
            return null;
        else if (t.right != null) {
            //右子树中最小的节点
            RBTreeNode<E> p = t.right;
            while (p.left != null) {
                p = p.left;
            }
            return p;
        }
        return null;
    }


    /*
    红黑树节点
     */
    static class RBTreeNode<E extends Comparable<? super E>> extends TreeNode<E, RBTreeNode<E>> {
        RBTreeNode<E> parent;
        boolean red;

        public RBTreeNode(E value) {
            super(value);
        }

        //查找操作
        RBTreeNode<E> find(E e) {
            if(e == null)
                return null;
            RBTreeNode<E> p = this; E v;
            while(p != null) {
                if((v = p.value) == null) {
                    return null;
                }
                else if(v == e || v.equals(e))
                    return p;
                else if(v.compareTo(e) > 0) {
                    p = p.left;
                } else {
                    p = p.right;
                }
            }
            return null;
        }
    }

    /*
    修复红黑树平衡条件：
    叔父节点一定要是黑色，之所以要修复是因为这条路径上有两个相邻的红色节点，那么要想办法通过将一个红色节点变成黑色
    多出来一个黑色怎么办，那么将祖父节点变成红色（祖父节点原来肯定是黑色，因为父节点是红色），因此只有在叔父节点是黑色时
    这样做才才是安全的（否则又多一组相邻的红色节点），父节点旋转成祖父节点，ok，性质（5）又保持了。
     */
    static <T extends Comparable<? super T>> RBTreeNode<T>
    rbInsertFixUp(RBTreeNode<T> root, RBTreeNode<T> x) {
        x.red = true;
        /*
        不变式：
        （1）循环开始时x一定是红色，因此如果xp是黑色就ok了；
        （2）至多破坏一个红黑树性质，要么是条件（2），要么是条件（4）；
        （3）xp如果是根节点，xp一定是黑色因此也ok；
         */
        for(RBTreeNode<T> xp, xpp, xppr, xppl;;) {
            //x是根节点，返回x
            if((xp = x.parent) == null) {
                x.red = false;
                return x;
            }
            //x的父节点是黑色或者xp是根节点
            else if(!xp.red || (xpp = xp.parent) == null) {
                return root;
            }
            //其余情况，分情况讨论，此时父节点为红色，违反了条件（4）
            else {
                //对称的两种情况
                if(xp == (xppl = xpp.left)) {
                    //case 1：叔父节点是红色，将祖父节点变为红色，父节点和叔父节点变为黑色
                    if((xppr = xpp.right) != null && xppr.red) {
                        xpp.red = true;
                        xp.red = false;
                        xppr.red = false;
                        x = xpp;
                    }
                    else {
                        //因为null表示叶节点为黑色，而不能通过修改xpp的颜色来满足条件（4），必须要旋转
                        //使xp代替xpp成为新的子树根，而xp是红色的，因此将xpp变为红色，xp变为黑色，可以满足条件（4）
                        //case 2：x为父节点的右儿子，先使用一次左旋，变为“一字形”
                        if(x == xp.right) {
                            rotateLeft(root, x = xp);
                            xpp = (xp = x.parent) == null ? null : xp.parent;
                        }
                        //case 3：x为父节点的左儿子，直接一次右旋将xp变为新的子树根，并修改颜色
                        if(xp != null) {
                            xp.red = false;
                            if(xpp != null) {
                                xpp.red = true;
                                rotateRight(root, xpp);
                            }
                        }
                    }
                }
                //对称情况，父节点是祖父节点的右儿子；
                else {
                    //case 1，叔父节点为红色，好办，直接变色
                    if(xppl != null && xppl.red) {
                        xpp.red = true;
                        xp.red = false;
                        xppl.red = false;
                        x = xpp;
                    }
                    else {
                        //case 2
                        if(x == xp.left) {
                            rotateRight(root, x = xp);
                            xpp = (xp = x.parent) == null ? null : xp.parent;
                        }
                        //case 3
                        if(xp != null) {
                            xp.red = false;
                            if(xpp != null) {
                                xpp.red = true;
                                rotateLeft(root, xpp);
                            }
                        }
                    }
                }
            }
        }
    }

    /*
    删除后修复平衡性
    x节点增加一层额外黑色，因此y是黑色节点被删除了，因此它的后台路径黑色数少了1。这样保持了性质5。
    删除循环只要考虑修复条件性质（1），（2），（4），这（2），（4）个性质的破坏都是由于x是红色的引起的；
    因此调整到一个合适的情况在x去掉一重额外黑色的时候（x是红-黑节点 或者 x是黑-黑节点路径上再调整增加一个节点），
    最后x变为黑色也就一起解决了。

    终止条件：
    （1）x执行红黑节点，这时就是将x着色成黑色；
    （2）x指向根节点，移除黑色就可以；
    （3）情况4,在x所在路径上增加一个黑色节点，就可以去色结束了；

    不变式：
    （1）子树中黑高没有发生变化，这也是为什么要添加“额外一重黑色”的原因，就是在概念上保证这一点；
     */
    static <T extends Comparable<? super T>> RBTreeNode<T>
    rbDeleteFixUp(RBTreeNode<T> root, RBTreeNode<T> x) {
        for (RBTreeNode<T> xp, xpr, xpl;;) {
            //x为空或者是根节点，因为始终保持性质（5）不变，这里直接退出是安全的
            if (x == null || x == root) {
                return root;
            }
            else if ((xp = x.parent) == null) {
                x.red = false; //保证根节点是黑色
                return x;
            }
            //x节点是“红-黑节点”，那么“去掉黑色”，变为黑色，黑色的数就保持了
            else if (x.red) {
                x.red = false;
                return root;
            }
            //对称两组情况
            else if ((xpl = xp.left) == x) {
                //情况一：x的兄弟节点是红色的
                if((xpr = xp.right) != null && xpr.red) {
                    xp.red = true;
                    xpr.red = false;
                    root = rotateLeft(root, xp);
                    xpr = (xp = x.parent) == null ? null : xp.right;
                }
                //如果没有兄弟节点，上滤是安全的，因为父节点和x是统一路径，并没有另外的分支了
                if(xpr == null)
                    x = xp;
                else { //兄弟节点是黑色
                    /*
                    情况二：兄弟节点是黑色，它的两个儿子都是黑色，上滤
                    兄弟节点变成红色，x设置为父节点，此时x是什么添加一重额外黑色，因此黑色数量没有减少
                    继续谋求增加一个黑色，这里儿子节点可以为null
                     */
                    RBTreeNode<T> sl = xpr.left, sr = xpr.right;
                    if((sl == null || !sl.red) &&
                            (sr == null || !sr.red)) {
                        xpr.red = true;
                        x = xp;
                    }
                    else {
                        /*
                        情况三：兄弟节点是黑色，它的右儿子是黑色的，左儿子是红色
                        */
                        if(sr == null || !sr.red) { //实际上这里sl只能是红色，sr为null也不影响
                            if(sl != null)
                                sl.red = false;
                            xpr.red = true; //兄弟节点变为红色
                            root = rotateRight(root, xpr);
                            xpr = (xp = x.parent) == null ? //这里xpr是原来的sl
                                    null : xp.right;
                        }
                        //情况四：兄弟节点是黑色，它的右儿子是红色
                        if(xpr != null) { //在此xpr不可能为null，防范型编程
                            xpr.red = (xp != null) && xp.red;
                            if((sr = xpr.right) != null) //有了上面情形三的处理，这里sr不为空必为红色
                                sr.red = false;
                        }
                        if(xp != null) {
                            xp.red = false;
                            root = rotateLeft(root, xp);
                        }
                        x = root;
                    }
                }
            } else {
                //情况一： x的兄弟节点红色
                if(xpl != null && xpl.red) {
                    xp.red = true;
                    xpl.red = false;
                    root = rotateRight(root, xp);
                    xpl = (xp = x.parent) == null ? null : xp.left;
                }
                if(xpl == null)
                    x = xp;
                else {
                    RBTreeNode<T> sl = xpl.left, sr = xpl.right;
                    //情形二：兄弟节点为黑色，它的两个儿子都为黑色
                    if((sl == null || !sl.red) &&
                            (sr == null || !sr.red)) {
                        xpl.red = true;
                        x = xp;
                    }
                    else {
                        //情形三：兄弟节点为黑色，它的左儿子黑色，右儿子为红色
                        if(sl == null || !sl.red) {
                            if(sr != null)
                                sr.red = false;
                            xpl.red = true;
                            root = rotateLeft(root, xpl);
                            xpl = (xp = x.parent) == null ?
                                    null : xp.left;
                        }
                        //情形四：
                        if(xpl != null) {
                            xpl.red = (xp != null) && xp.red;
                            if((sl = xpl.left) != null)
                                sl.red = false;
                        }
                        if(xp != null) {
                            xp.red = false;
                            root = rotateLeft(root, xp);
                        }
                    }
                }
            }
        }
    }

    //左旋
    static <T extends Comparable<? super T>> RBTreeNode<T>
    rotateLeft(RBTreeNode<T> root, RBTreeNode<T> p) {
        RBTreeNode<T> r, pp, rl;
        if(p != null && (r = p.right) != null) {
            if((rl = p.right = r.left) != null)
                rl.parent = p;
            if((pp = r.parent = p.parent) == null)
                (root = r).red = false;
            else if(pp.left == p)
                pp.left = r;
            else
                pp.right = r;
            r.left = p;
            p.parent = r;
        }

        return root;
    }

    //右旋
    static <T extends Comparable<? super T>> RBTreeNode<T>
    rotateRight(RBTreeNode<T> root, RBTreeNode<T> p) {
        RBTreeNode<T> l, pp, lr;
        if(p != null && (l = p.left) != null) {
            if((lr = p.left = l.right) != null)
                lr.parent = p;
            if((pp = l.parent = p.parent) == null)
                (root = l).red = false;
            else if(pp.left == p)
                pp.left = l;
            else
                pp.right = l;
            l.right = p;
            p.parent = l;
        }
        return root;
    }

    public static void main(String[] args) {

    }
}
