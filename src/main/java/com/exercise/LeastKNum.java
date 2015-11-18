package com.exercise;

import com.basic.algorithms.SortUtil;

import java.util.*;

/**
 * 输入n个整数，找出其中最小的K个数。例如输入4,5,1,6,2,7,3,8这8个数字，则最小的4个数字是1,2,3,4。
 *
 * 思路：
 * 有2种方法：一是用最大堆；二是用类似快排划分的分治法；
 * 方法二修改了数组，如果不能修改数组，还是用方法一比较好；
 *
 * Created by yjh on 15-11-3.
 */
public class LeastKNum {
    public static ArrayList<Integer> GetLeastNumbers_Solution(int [] input, int k) {
        ArrayList<Integer> res = new ArrayList<Integer>();
        if(input == null || k == 0 || input.length < k)
            return res;
        else {
            PriorityQueue<Integer> priorityQueue = new PriorityQueue<Integer>(k, new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    if(o1.equals(o2) )
                        return 0;
                    else if(o1 < o2)
                        return 1;
                    else return -1;
                }
            });
            for(int n : input) {
                if(priorityQueue.size() < k) {
                    priorityQueue.add(n);
                } else {
                    Integer m = priorityQueue.peek();
                    if(m > n) {
                        priorityQueue.poll();
                        priorityQueue.add(n);
                    }
                }
            }
            List s = Arrays.asList(priorityQueue.toArray());
            res.addAll(s);
        }
        Collections.sort(res);
        return res;
    }

    public static ArrayList<Integer> GetLeastNumbers_Solution1(int [] input, int k) {
        ArrayList<Integer> res = new ArrayList<Integer>();
        if(input == null || input.length < k || k == 0)
            return res;
        else {
            int start = 0, end = input.length - 1;
            int m = Partition(input, start, end);
            while(m != k - 1) {
                if(m < k) {
                    start = m + 1;

                } else {
                    end = m - 1;
                }
                m = Partition(input, start, end);
            }
            for(int i = 0; i < k; ++i) {
                res.add(input[i]);
            }
            return res;
        }
    }

    public static int Partition(int[] s, int l, int h) {
        int x = s[l];
        int i = l;
        int j = h;
        while(i < j) {
            while(s[j] > x) {
                --j;
            }
            if(i < j) {
                s[i] = s[j];
                ++i;
            }
            while(s[i] < x) {
                ++i;
            }
            if(i < j) {
                s[j] = s[i];
                --j;
            }
        }
        s[i] = x;
        return i;
    }

    //选择第k小的值
    public static void swap(int[] input, int i, int j) {
        int temp = input[i];
        input[i] = input[j];
        input[j] = temp;
    }
    public static int median3(int[] input, int left, int right) {
        int center = (left + right) >> 1;
        if(input[left] > input[right])
            swap(input, left, right);
        if(input[left] > input[center])
            swap(input, left, center);
        if(input[center] > input[right])
            swap(input, center, right);
        swap(input, center, right - 1);
        return input[right - 1];
    }

    private static final int CutOff = 3;
    public static int QSelect(int[] input, int k) {
        if(input == null || input.length < k || k <= 0)
            throw new RuntimeException("参数k必须小于等于数组长度");
        QSelect(input, k - 1, 0, input.length - 1);
        return input[k - 1];
    }
    public static void QSelect(int[] input, int k, int left, int right) {
        if(left + CutOff <= right) {
            int i = left, j = right - 1;
            int pivot = median3(input, left, right);
            for(;;) {
                while(input[++i] < pivot){}
                while(input[--j] > pivot){}
                if(i < j)
                    swap(input, i, j);
                else
                    break;
            }
            swap(input, i, right - 1);
            if(k < i)
                QSelect(input, k, left, i - 1);
            else if(k > i)
                QSelect(input, k, i + 1, right);
        } else {
            SortUtil.insertionSort(input, left, right); //如果不要排序，就不用加一句
        }
    }

    public static void main(String[] args) {
        int[] num = {4,5,1,6,2,7,3,8};
        System.out.println(QSelect(num, 3));
        System.out.println(Arrays.toString(num));
    }
}
