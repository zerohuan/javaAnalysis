package com.exercise.standard;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * 数据流中的中位数
 * 如何得到一个数据流中的中位数？如果从数据流中读出奇数个数值，那么中位数就是所有数值排序之后位于中间的数值。
 * 如果从数据流中读出偶数个数值，那么中位数就是所有数值排序之后中间两个数的平均值。
 *
 * 解法：
 *
 *
 * Created by yjh on 16-2-4.
 */
public class Ex_64_GetMedian {
    private PriorityQueue<Integer> maxHeap = new PriorityQueue<Integer>(10, new Comparator<Integer>() {
        @Override
        public int compare(Integer i1, Integer i2) {
            if (i1 < i2)
                return 1;
            else if (i1 > i2)
                return -1;
            else
                return 0;
        }
    });
    private PriorityQueue<Integer> minHeap = new PriorityQueue<Integer>();
    private int totalCount;

    public void Insert(Integer num) {
        totalCount++;
        if ((totalCount & 1) == 1) { //奇数
            if (maxHeap.size() > 0) {
                int maxTop = maxHeap.peek();
                if (num < maxTop) {
                    maxHeap.poll();
                    maxHeap.add(num);
                    minHeap.add(maxTop);
                    return;
                }
            }
            minHeap.add(num);
        } else { //偶数
            int minTop = minHeap.peek();
            if (num > minTop) {
                minHeap.poll();
                minHeap.add(num);
                maxHeap.add(minTop);
            } else
                maxHeap.add(num);
        }
    }
    public Double GetMedian() {
        Integer minTop = minHeap.peek();
        if ((totalCount & 1) == 1) { //奇数
            return minTop.doubleValue();
        } else {
            return (minTop + maxHeap.peek()) / 2.0;
        }
    }

    public static void main(String[] args) {
        Ex_64_GetMedian t = new Ex_64_GetMedian();
        t.Insert(5);
        System.out.println(t.GetMedian());
        t.Insert(2);
        System.out.println(t.GetMedian());
        t.Insert(3);
        System.out.println(t.GetMedian());
        t.Insert(4);
        System.out.println(t.GetMedian());
        t.Insert(1);
        System.out.println(t.GetMedian());
        t.Insert(6);
        System.out.println(t.maxHeap + " " + t.minHeap);
        System.out.println(t.GetMedian());
        t.Insert(7);
        System.out.println(t.GetMedian());
        t.Insert(0);
        System.out.println(t.GetMedian());
        t.Insert(8);
        System.out.println(t.GetMedian());
    }
    /*
    测试用例:
    [5,2,3,4,1,6,7,0,8]

    对应输出应该为:

    "5.00 3.50 3.00 3.50 3.00 3.50 4.00 3.50 4.00 "

    你的输出为:

    "5.00 3.50 2.00 3.00 1.00 3.50 1.00 0.50 0.00 "
     */
}
