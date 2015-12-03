package com.exercise.temp;

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
}
