package com.exercise.backtracking;

import java.util.*;

/**
 * 幂集问题
 * Created by yjh on 15-12-20.
 */
public class PowerSetProblem {
    static class Node {
        int k; //位置
        boolean status; //在解中的状态

        public Node(int k, boolean status) {
            this.k = k;
            this.status = status;
        }
    }

    /**
     * 迭代解法
     * @param originSet 输入集合
     * @return
     */
    public static Set<Set<Integer>> powerSet(List<Integer> originSet) {
        Set<Set<Integer>> res = new HashSet<>();

        if (originSet != null && !originSet.isEmpty()) {
            int len;
            boolean[] status = new boolean[len = originSet.size()];
            Stack<Node> stack = new Stack<>();
            int k = -1;
            while (k < len) {
                if (k < len - 1) {
                    status[++k] = true;
                    stack.push(new Node(k, false));
                }
                else {
                    //输出一个解
                    Set<Integer> set = new HashSet<>();
                    for (int i = 0; i < status.length; ++i) {
                        if(status[i])
                            set.add(originSet.get(i));
                    }
                    res.add(set);
                    if(stack.isEmpty())
                        break;
                    Node n = stack.pop(); //回溯
                    k = n.k;
                    status[k] = n.status;
                }
            }
        }

        return res;
    }

    /*
    不用辅助栈
     */
    public static Set<Set<Integer>> powerSet1(List<Integer> originSet) {
        Set<Set<Integer>> res = new HashSet<>();

        if (originSet != null && !originSet.isEmpty()) {
            int len;
            int[] status = new int[len = originSet.size()]; //状态值：1 该元素在集合中；2 该元素不再集合中；
            int k = 0;
            while (k >= 0) {
                ++status[k];
                if (status[k] <= 2 && k == len -1) { //找到一个解
                    Set<Integer> s = new HashSet<>();
                    for (int i = 0; i < len; ++i)
                        if (status[i] == 1)
                            s.add(originSet.get(i));
                    res.add(s);
                } else {
                    if (status[k] > 2) --k; //回溯
                    else status[++k] = 0; //下一个
                }
            }
        }
        return res;
    }

    //因为解空间可以表示为一个满二叉树，可以用二进制数法
    public static Set<Set<Integer>> powerSet2(List<Integer> originSet) {
        Set<Set<Integer>> res = new HashSet<>();
        if (originSet != null && !originSet.isEmpty()) {
            int len = originSet.size(), max = (1 << len) - 1;
            for (int i = 1; i <= max; ++i){
                Set<Integer> s = new HashSet<>();
                int temp, k = 0;
                while ((temp = (1 << k)) < max) {
                    if ((temp & i) == temp)
                        s.add(originSet.get(k));
                    ++k;
                }
                res.add(s);
            }
        }
        return res;
    }

    /**
     * 递归解法
     * @param originSet 输入集合
     * @param k 位置
     * @param status 解状态
     * @param res 结果集合
     */
    public static void powerSet(List<Integer> originSet, int k, boolean[] status, Set<Set<Integer>> res) {
        if (k == originSet.size()) {
            Set<Integer> set = new HashSet<>();
            for (int i = 0; i < status.length; ++i) {
                if(status[i])
                    set.add(originSet.get(i));
            }
            res.add(set);
        }
        else {
            status[k] = true;
            powerSet(originSet, k + 1, status, res);
            status[k] = false;
            powerSet(originSet, k + 1, status, res);
        }
    }

    public static void main(String[] args) {
        Set<Set<Integer>> res;
        List<Integer> list;
        powerSet(list = new ArrayList<>(Arrays.asList(1,2,3)), 0, new boolean[3], res = new HashSet<>());
        System.out.println(res);

        System.out.println(powerSet2(list));
    }
}
