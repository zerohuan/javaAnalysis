package com.exercise;

/**
 * Created by yjh on 15-12-3.
 */
public class PostOrderCheckOfBinaryTree {

    private static boolean isPostOrder(int[] order, int l, int r) {
        if(l >= r)
            return true;
        int root = order[r];
        int i;
        for(i = r - 1; i >= l; --i) {
            if(order[i] < root)
                break;
        }
        int mid = i;
        for(; i >= l; --i) {
            if(order[i] > root)
                return false;
        }
        return isPostOrder(order, l, mid) && isPostOrder(order, mid + 1, r - 1);
    }

    public static boolean isPostOrder(int[] order) {
        int len;
        if(order == null || (len = order.length) == 0) {
            return false;
        }

        return isPostOrder(order, 0, len - 1);
    }

    public static boolean isPreOrder(int[] order) {
        int len;
        if(order == null || (len = order.length) == 0) {
            return false;
        }
        return isPreOrder(order, 0, len - 1);
    }

    private static boolean isPreOrder(int[] order, int l, int r) {
        if(l >= r)
            return true;
        int root = order[l];
        int i;
        //遍历左子树
        for(i = l + 1; i <= r; i++)
            if(order[i] > root)
                break;
        for(int j = i; j <= r; j++)
            if(order[j] < root)
                return false;
        return isPreOrder(order, l + 1, i) && isPreOrder(order, i + 1, r);
    }


    public static void main(String[] args) {
        System.out.println(isPostOrder(new int[] {5,4,3,2,1}));
        System.out.println(isPreOrder(new int[] {7,4,6,5,8,1,9}));
    }
}
