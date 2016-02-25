package com.exercise.dp;

/**
 * 对一个正整数作如下操作:如果是偶数则除以 2,如果是奇数则加 1,如此进行直到 1 时操作停止,求经过 9 次操作变为 1 的数有多少个?
 * 解法：
 * 反过来考虑，从1开始进行计算，如果当前是奇数，那么只能×2；如果是偶数，可以×2，也可以+1；
 * 故而可以得到状态转移方程：
 * 执行第i次得到：
 * oddCount[i] = evenCount[i-1];
 * evenCount[i] = oddCount[i-1] + evenCount[i-1];
 * totalCount[i] = oddCount[i] + evenCount[i];
 *
 * 进一步带换可以得到：
 * totalCount[i] = evenCount[i-1] + oddCount[i-1] + evenCount[i-1]
 *               = totalCount[i-1] + totalCount[i-2];
 * 即为斐波那契数列
 *
 * 注意：
 *
 * Created by yjh on 16-2-24.
 */
public class EvenDoubleOddInc {
    private static int numCount(int count) {
        if (count == 0)
            return 1;
        int[] oddCount = new int[count + 1];
        int[] evenCount = new int[count + 1];
        oddCount[1] = 1;
        evenCount[1] = 0;
        for (int i = 2; i <= count; ++i) {
            oddCount[i] = evenCount[i-1];
            evenCount[i] = oddCount[i-1] + evenCount[i-1];
        }
        return oddCount[count] + evenCount[count];
    }
    public static void main(String[] args) {
        System.out.println(numCount(9));
    }
}
