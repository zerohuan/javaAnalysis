package com.exercise.standard;

/**
 * 统计一个数字在排序数组中出现的次数。
 *
 * 解法：
 * 一个容易想到的思路是使用二分查找法向找到一个k，再分别向两个方向统计k的个数；
 * 这个方法的时间：O(n)；
 *
 * 改进：
 * 用二分法分别找到第一个k和最后一个k的位置，这样就可以得到k的个数了；
 * 这个方法时间：O(lgn)；
 *
 * Created by yjh on 16-2-2.
 */
public class Ex_38_GetNumberOfK {
    //简单但不是最好的解法：用二分查找先找到，再从两个方向统计次数
    public static int GetNumberOfK(int[] array , int k) {
        int len;
        if (array == null || (len = array.length) == 0)
            return 0;
        int l = 0, r = len - 1;
        int count = 0;
        while (l <= r) {
            int mid = (l + r) >> 1;
            if (array[mid] == k) {
                //进行查找
                int i = mid, j = mid - 1;
                while (i < len && array[i] == k) {
                    ++count;
                    ++i;
                }
                while (j >= 0 && array[j] == k) {
                    ++count;
                    --j;
                }
                return count;
            } else if (array[mid] < k)
                l = mid + 1;
            else
                r = mid - 1;
        }
        return count;
    }

    /*
    解法：
     */
    public static int GetNumberOfK_2(int[] array , int k) {
        if (array == null || array.length == 0)
            return -1;
        int lastIndex = GetNumberOfLastK(array, k);
        int firstIndex = GetNumberOfFirstK(array, k);
        System.out.println(lastIndex + " " + firstIndex);
        if (lastIndex != -1)
            return lastIndex - firstIndex + 1;
        else
            return 0;
    }
    //找到第一个k
    private static int GetNumberOfFirstK(int[] array, int k) {
        int len;
        if (array == null || (len = array.length) == 0)
            return -1;
        int l = 0, r = len - 1;
        while (l <= r) {
            int mid = (l + r) >> 1;
            if (array[mid] == k) {
                if (mid == 0 || array[mid - 1] != k)
                    return mid;
                else
                    r = mid - 1;
            } else if (array[mid] < k) {
                l = mid + 1;
            } else {
                r = mid - 1;
            }
        }
        return -1;
    }

    private static int GetNumberOfLastK(int[] array, int k) {
        int len;
        if (array == null || (len = array.length) == 0)
            return -1;
        int l = 0, r = len - 1;
        while (l <= r) {
            int mid = (l + r) >> 1;
            if (array[mid] == k) {
                if (mid == len - 1 || array[mid + 1] != k)
                    return mid;
                else
                    l = mid + 1;
            } else if (array[mid] < k) {
                l = mid + 1;
            } else {
                r = mid - 1;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        System.out.println(GetNumberOfK_2(new int[]{3}, 3));
    }
}
