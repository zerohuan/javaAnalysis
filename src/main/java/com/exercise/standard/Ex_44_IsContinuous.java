package com.exercise.standard;

/**
 * 扑克牌的顺子
 * 用数字代表牌，0可以表示任何数字，判断输入是不是一个顺子。
 *
 * 解法：
 * 用一个大小为14的哈希表计算输入数字各自的个数；
 * 如果出现了个数大于1的说明不可能是顺子；
 * 否则如果非0的数字之间的空档如果可以被0填补，那就说明可以是一个顺子。
 *
 * Created by yjh on 16-2-3.
 */
public class Ex_44_IsContinuous {
    public static boolean isContinuous(int [] numbers) {
        int len;
        if (numbers == null || (len = numbers.length) <= 0)
            return false;
        int[] bucket = new int[14];
        for (int i : numbers) {
            ++bucket[i];
        }
        int zeroCount = bucket[0];
        int first = 0, last = 0;
        for (int i = 1; i < bucket.length; ++i) {
            if (bucket[i] > 1) return false;
            if (bucket[i] != 0) {
                if (first == 0)
                    first = i;
                last = i;
            }
        }
        return last - first + zeroCount == len - 1 || last - first == len - 1;
    }

    public static void main(String[] args) {
        System.out.println(isContinuous(new int[]{0,3,2,6,4}));
    }
}
