package com.exercise.standard;

/**
 * 圆圈中最后剩下的数（约瑟夫环问题）
 * 0,1,...,n-1这n个数字排成一个圆圈，从数字0开始每次从这个圆圈里删除第m个数字。求出这个圆圈里剩下的最后一个数字。
 *
 * 解法1，用链表模拟，时间：O(mn)，空间：O(n)
 *
 * 解法2：找到删除的规律直接得到最后答案
 * 设p = f(n,m)为最终那个数，首先如果找到从n个数变为n-1个数p的递归式，那么这个问题就可解
 * 设k = (m-1)%n为第一个删除的数，因为删除之后又从0开始计数，那么可以得到各个数在原来n个数中的编号和在n-1之后的编号映射：
 * k+1 -- 0
 * k+2 -- 1
 * ...
 * n-1 -- n-k-2
 * 0   -- n-k-1
 * 1   -- n-k
 * ...
 * k-1 -- n-2
 * 得到映射关系：p(x) = (x-k-1)%n；那么逆映射是p^-1(x) = (x+k+1)%n;
 * f'(n-1,m) = p^-1(f(n-1,m))=[f(n-1,m)+k+1]%n，将k=(m-1)%n代入；
 * 得到：f(n,m)= (1)0; (n=1)
 *              (2)f'(n-1,m)=[f(n-1,m)+m]%n; (n>1)
 *
 * Created by yjh on 16-2-3.
 */
public class Ex_45_LastRemaining {
    //用递归式处理
    public static int LastRemaining_Solution_1(int n, int m) {
        if (n <= 0 && m <= 0)
            return -1;
        int last = 0;
        for (int i = 2; i <= n - 1; ++i)
            last = (last + m) % i;
        return last;
    }

    //用链表模拟
    public static int LastRemaining_Solution_2(int n, int m) {
        if (n <= 0 || m <= 0)
            return -1;
        Node list = new Node(0), p = list;
        for (int i = 1; i < n; ++i) {
            Node next = new Node(i);
            p.next = next;
            p = next;
        }
        //循环链表
        p.next = list;
        Node prev = p;
        p = list;
        int curIndex = 0;
        while (n > 1) {
            if (curIndex++ == m - 1) {
                //删除节点
                prev.next = p.next;
                p = p.next;
                n--;
                curIndex = 0;
            } else {
                prev = p;
                p = p.next;
            }
        }
        return p.val;
    }
    private static class Node {
        private int val;
        private Node next;

        public Node(int val) {
            this.val = val;
        }
    }

    public static void main(String[] args) {

    }
}
