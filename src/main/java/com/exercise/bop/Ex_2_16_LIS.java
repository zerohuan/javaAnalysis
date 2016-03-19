package com.exercise.bop;

/**
 * 最长递增子序列，输入数组A
 * 动态规划：设计两个状态数组：LIS和MAX
 * MAX[LIS[i]]，LIS[i]为已a[i]最大元素的最长递增子序列长度，MAX[LIS[i]]为使得长度为LIS[i]的递增子序列的最大元素的最小值；
 * 意思是可能有多个递增子序列长度为LIS[i]，我们需要保存序列中最大元素也就是最后一个元素，让后面的元素进行比较，大于这个最大元素
 * 说明可以加入这个递增子序列，但是我们应当保存所有长度为LIS[i]的序列最后一个元素的最小值；
 *
 * Created by yjh on 2016/3/17.
 */
public class Ex_2_16_LIS {
    public static int lis(int[] a) {
        int len;
        if (a != null && (len = a.length) > 0) {
            int[] max = new int[len + 1];
            int[] lis = new int[len];
            max[1] = a[0];
            max[0] = Integer.MIN_VALUE;
            int maxLIS = 1;
            for (int i = 1; i < len; ++i) {
                int j;
                for (j = maxLIS; j >= 0; --j) {
                    if (a[i] > max[j]) {
                        lis[i] = j + 1;
                        break;
                    }
                }
                //大于当前最大的lis
                if (lis[i] > maxLIS) {
                    max[lis[i]] = a[i];
                    maxLIS = lis[i];
                } else if(a[i] > max[j] && a[i] < max[j+1]) {
                    max[j+1] = a[i];
                }
            }
        }
        return 0;
    }
}
