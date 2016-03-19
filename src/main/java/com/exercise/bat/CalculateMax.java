package com.exercise.bat;

/**
 * С��
 *
 * ���֮�£����ܷɡ������й�����ţ�У����ν����������ꡱ�� ����һ���ع���ʷ�Ļ��ᣬ��֪һ֧��Ʊ����n��ļ۸����ƣ�
 * �Գ���Ϊn�����������ʾ�������е�i��Ԫ�أ�prices[i]������ù�Ʊ��i��Ĺɼۡ� ������һ��ʼû�й�Ʊ������������������1
 * �ɶ�������1�ɵĻ��ᣬ��������ǰһ��Ҫ�ȱ�֤����û�й�Ʊ�������ν��׻��ᶼ����������Ϊ0��
 * ����㷨���������ܻ�õ�������档 ������ֵ��Χ��2<=n<=100,0<=prices[i]<=100
 *
 * �ⷨ����̬�滮
 *
 *
 * Created by lenovo on 2016/3/12.
 */
public class CalculateMax {
    public static int calculateMax(int[] prices) {
        int len;
        if (prices == null || (len = prices.length) == 0)
            return 0;
        int[] p = new int[len - 1];
        for (int i = 0; i < p.length; ++i)
            p[i] = prices[i+1] - prices[i];
        int sum = 0, max = 0, subMax = 0;
        for (int i = 0; i < p.length; ++i) {
            boolean flag = sum > 0;
            sum += p[i];
            if (sum <= 0) {
                sum = 0;
            } else {
                if (sum > max) {
                    int temp = max;
                    max = sum;
                    if (!flag && temp > subMax)
                        subMax = temp;
                } else if (sum > subMax) {
                    subMax = sum;
                }
            }
        }
        System.out.println(max + " " + subMax);
        return max + subMax;
    }

    public static void main(String[] args) {
        System.out.println(calculateMax(new int[]{5,15,56,26,62,65,57,69}));
    }
}
