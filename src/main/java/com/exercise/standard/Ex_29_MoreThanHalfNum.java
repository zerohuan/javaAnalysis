package com.exercise.standard;

import com.exercise.util.SwapUtil;

/**
 * 数组中出现次数超过一半的数字
 * 关键词：时间效率，空间效率的考虑
 *
 * 方法一：基于划分，时间O(n)；
 * 方法二：基于HashMap，时间O(n)，O(n)；
 * 方法三：联机算法，根据数组的特点，
 *
 * Created by yjh on 16-1-12.
 */
public class Ex_29_MoreThanHalfNum {
    /*
    基于划分（快速选择）的方法，O(n)会修改原数组
    划分，三元取中选取枢纽元，找到中位数，再判断是否出现次数大于一半
     */
    public static int moreThanHalfNum(int[] n) {
        if (SwapUtil.checkInvalidArray(n))
            return 0;
        int len = n.length;
        int mid = len >> 1;
        int start = 0, end = len - 1;
        int index = partition(n, start, end);
        while (index != mid) {
            if (index > mid)
                end = index - 1;
            else
                start = index + 1;
            index = partition(n, start, end);
        }
        int result = n[mid], times = 0;
        for (int i : n)
            if (n[i] == result)
                times++;
        return times > (len >> 1) ? result : 0;
    }
    public static int partition(int[] n, int l, int r) {
        int pivot = median3(n, l, r), p = l, q = r - 1;
        while (p < q) {
            while (pivot > n[++p]);
            while (pivot < n[--q]);
            if (p > q)
                break;
            else
                SwapUtil.swap(n, p, q);
        }
        SwapUtil.swap(n, p, r - 1);
        return p;
    }
    public static int median3(int[] n, int l, int r) {
        int mid = (l + r) >> 1;
        if (n[mid] < n[l])
            SwapUtil.swap(n, l, mid);
        if (n[mid] > n[r])
            SwapUtil.swap(n, mid, r);
        if (n[l] > n[r])
            SwapUtil.swap(n, l, r);
        SwapUtil.swap(n, r - 1, mid);
        return n[r - 1];
    }

    /*
    方法三：根据数组特点，O(n)
    如果存在这样一个数，出现的次数超过数组长度的一半，那该数出现的次数超过其他所有数次数之和
    因此采用如下方法，当该数存在是可以得到它：
    保存两个值，一个是次数（count），一个是数字cur；
    遍历数组，当遍历到的数字和cur不相等count - 1，否则count + 1；
    如果count == 0，那cur保存当前遍历到的数；
    最后检查cur是否符合要求
     */
    public static int moreThanHalfNum1(int[] n) {
        if (SwapUtil.checkInvalidArray(n))
            return 0;
        int count = 1, cur = n[0], len;
        for (int i = 1; i < (len = n.length); ++i) {
            if (count == 0) {
                cur = n[i];
                count = 1;
            } else if (n[i] == cur)
                ++count;
            else
                --count;
        }
        int times = 0;
        for (int i : n)
            if (n[i] == cur)
                times++;
        return times > len >> 1 ? cur : 0;
    }

    public static void main(String[] args) {
        System.out.println(moreThanHalfNum1(new int[]{2,2,2,1,3,4,5,2,2,2}));
    }
}
