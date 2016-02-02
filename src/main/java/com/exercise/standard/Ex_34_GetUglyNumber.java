package com.exercise.standard;

/**
 * 把只包含因子2、3和5的数称作丑数（Ugly Number）。例如6、8都是丑数，但14不是，
 * 因为它包含因子7。 习惯上我们把1当做是第一个丑数。求按从小到大的顺序的第N个丑数。
 *
 * 解法：
 * 如果使用穷举法，时间复杂度为O(n)。但还可以通过抽数的规律减少不必要的计算，
 * 通过一个数组保存已经找到的丑数，根据已有的丑数按大小顺序计算出可能下一个丑数。
 * 每个丑数都是之前某个丑数乘以2或3或5得到的。具体是乘以多少，要根据大小进行选择，所以
 * 下面的解法通过t2,t3,t5分别保三种计算路径的位置以供选择。
 *
 * Created by yjh on 16-2-1.
 */
public class Ex_34_GetUglyNumber {
    public static int GetUglyNumber_Solution(int index) {
        if (index == 0)
            return 0;

        int[] uglyNumbers = new int[index];
        int t2, t3, t5;
        int nextIndex = 1;

        uglyNumbers[0] = 1;
        t2 = t3 = t5 = 0;

        while (nextIndex < index) {
            uglyNumbers[nextIndex] = Math.min(Math.min(uglyNumbers[t2] * 2, uglyNumbers[t3] * 3), uglyNumbers[t5] * 5);

            //更新t值
            while (uglyNumbers[t2] * 2 <= uglyNumbers[nextIndex])
                ++t2;
            while (uglyNumbers[t3] * 3 <= uglyNumbers[nextIndex])
                ++t3;
            while (uglyNumbers[t5] * 5 <= uglyNumbers[nextIndex])
                ++t5;
            ++nextIndex;
        }

        return uglyNumbers[index - 1];
    }

    public static void main(String[] args) {
        System.out.println(GetUglyNumber_Solution(6));
    }
}
