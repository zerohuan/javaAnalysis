package com.exercise.dp;

/**
 * �ϲ���������
 * ԭ��
 * �����������ں�Ϊָ�����ֵĺϲ�����
 * ����[5, 5, 10, 2, 3] �ϲ�ֵΪ 15 : ��4�� �� (5 + 10, 5 + 10, 5 + 5 + 2 + 3, 10 + 2 + 3)
 *
 * �ⷨ��
 * ��̬�滮�ⷨ
 * ״̬ת�Ʒ��̣�
 * dp[0][0] = 1;
 * dp[n][m] = dp[n-1][m] + dp[n-1][m-num[n-1]];
 * dp[n][m]��ʾ������ǰn�����кϲ���Ϊm���������
 *
 * Created by yjh on 2016/2/23.
 */
public class CombinationSum {
    public static int combinationSum(int[] num, int sum) {
        int len;
        if (num == null || (len = num.length) == 0 || sum < 0)
            return 0;
        int[][] dp = new int[len+1][sum+1];
        dp[0][0] = 1;
        for (int n = 1; n <= len; n++) {
            for (int m = 0; m <= sum; m++) {
                if (m >= num[n-1])
                    dp[n][m]=dp[n-1][m]+dp[n-1][m-num[n-1]];
                else
                    dp[n][m] = dp[n-1][m];
            }
        }
        return dp[len][sum];
    }

    public static void main(String[] args) {
        System.out.println(combinationSum(new int[]{5,5,10,2,3}, 15));
    }
}
