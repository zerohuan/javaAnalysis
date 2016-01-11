package com.exercise.temp;

import java.util.Arrays;
import java.util.Stack;

/**
 * 练习草稿纸
 *
 * Created by yjh on 15-11-28.
 */
public final class Temp {
    /*
    带有min函数的栈
     */
    private static class StackWithMin {
        private Stack<Integer> m = new Stack<>();
        private Stack<Integer> assist = new Stack<>();

        public void push(int e) {
            m.push(e);
            if(!assist.isEmpty()) {
                int currentMin = assist.peek();
                if(e <= currentMin)
                    assist.push(e);
            } else {
                assist.push(e);
            }
        }

        public int pop() {
            int e = m.pop();
            int currentMin = assist.peek();
            if(e <= currentMin)
                assist.pop();
            return e;
        }

        public int min() {
            return assist.peek();
        }

        public static void main(String[] args) {
            StackWithMin withMin = new StackWithMin();
            withMin.push(3);
            withMin.push(4);
            withMin.push(1);
            withMin.push(1);
            withMin.push(2);
            withMin.push(5);
            withMin.pop();
            withMin.pop();
            withMin.pop();
            System.out.println(withMin.min());
        }
    }

    private static class IsPopOrder {
        public static boolean isPopOrder(int[] pushOrder, int[] popOrder) {
            int len;
            if(pushOrder == null || popOrder == null || (len = pushOrder.length) == 0
                    || pushOrder.length != popOrder.length)
                return false;

            Stack<Integer> s = new Stack<>();
            int i = 0;
            for(int j = 0; j < len; j++) {
                s.push(pushOrder[j]);
                while(!s.isEmpty() && s.peek() == popOrder[i]) {
                    s.pop();
                    i++;
                }
            }

            return s.isEmpty();
        }

        public static void main(String[] args) {
            int[] o1 = {1,2,3,4,5}, o2 = {4,5,3,1,2};
            System.out.println(isPopOrder(o1, o2));
        }
    }

    private static boolean isValid(int[] x, int n) {
        for (int i = 0; i < n; ++i) {
            if (x[i] == x[n] || Math.abs(n - i) == Math.abs(x[n] - x[i]))
                return false;
        }
        return true;
    }
    private static class EightQueen {
        private static int inQueen(int n) {
            int[] x = new int[n];
            int k = 0, totalCount = 0;
            while (k >= 0) {
                x[k]++;
                while (x[k] <= n && !isValid(x, k))
                    ++x[k];
                if (x[k] <= n && k == n - 1) {
                    ++totalCount;
                    System.out.println(Arrays.toString(x));
                }
                else {
                    if (x[k] > n) --k; //回溯，每当超过n就向前回溯，可以想象整个过程
                    else
                        x[++k] = 0; //准备放置下一个
                }
            }
            return totalCount;
        }

        private static int rnQueen(int[] x, int k) {
            int n, total = 0;
            if ((n = x.length) == k) { //一个解
                ++total;
                System.out.println(Arrays.toString(x));
            }
            else {
                for (int i = 1; i <= n; ++i) {
                    x[k] = i;
                    if (isValid(x, k))
                        total += rnQueen(x, k + 1);
                }
            }
            return total;
        }

        public static void main(String[] args) {
            System.out.println(rnQueen(new int[8], 0));
        }
    }
}
