package com.exercise.standard;

import java.util.ArrayList;

/*
 * 问题：和为S的两个数字
 * 输入一个递增排序的数组和一个数字S，在数组中查找两个数，使得他们的和正好是S，
 * 如果有多对数字的和等于S，输出两个数的乘积最小的。
 *
 * 解法：
 * 使用两个边界值l和h分别表示较小的数和较大的数。
 * 主要l和h变化的方向，当(l+h)小于sum，l增大，大于sum时h减少。
 *
 * 我觉得要理解这种做法可以将所有可能的组合都遍历到，需要进行证明：
 * (l+h) < sum时，l增大。
 * 那么此时是否存在x < l，y < h使得x + y == sum呢？
 * 用反证法：如果x + y == sum成立，那么必然有x + h > sum，那么h必然减少直到h == y。
 *
 * 问题：和为S的连续正数序列
 *
 * 解法：
 * 同样使用l和h两个边界控制序列的范围。l和h都只能增大不能减少，初始为l=1,h=2，
 * 这样可以保证所有可能的序列都能被遍历到。
 * 用一个curSum存储当前序列的和以免重复计算，当值小于sum时，h++；大于sum时l--；
 * 找到一个值之后，h++向后继续查找新的可能的序列。
 *
 * 时间：O(n)
 *
 * 注意：其实序列增长的方向不止一种，但一定要使得l和h的始终保持一种增长方向，便于控制，保证可以结束
 *
 * Created by yjh on 16-2-2.
 */
public class Ex_41_FindContinuousSequence {
    public static ArrayList<Integer> FindNumbersWithSum(int [] array,int sum) {
        ArrayList<Integer> res = new ArrayList<>();
        int len;
        if (array != null && (len = array.length) != 0) {
            int l = 0, h = len - 1, tempS;
            while (l < h && (tempS = array[l] + array[h]) != sum) {
                if (tempS < sum)
                    ++l;
                else
                    --h;
            }
            if (l < h) {
                res.add(array[l]);
                res.add(array[h]);
            }
        }

        return res;
    }

    public static ArrayList<ArrayList<Integer>> FindContinuousSequence(int sum) {
        ArrayList<ArrayList<Integer>> res = new ArrayList<>();

        if (sum > 2) {
            int l = 1, h = 2, maxS = sum >> 1, curSum = 3;
            while (l <= maxS) {
                if (curSum == sum) {
                    res.add(list(l, h));
                    ++h;
                    curSum += h;
                } else if (curSum < sum) {
                    ++h;
                    curSum += h;
                } else {
                    curSum -= l;
                    ++l;
                }
            }
        }

        return res;
    }
    private static ArrayList<Integer> list(int l, int h) {
        ArrayList<Integer> res = new ArrayList<>();
        for (int i = l; i <= h; ++i) {
            res.add(i);
        }
        return res;
    }

    public static void main(String[] args) {
        System.out.println(FindContinuousSequence(15));
    }
}
