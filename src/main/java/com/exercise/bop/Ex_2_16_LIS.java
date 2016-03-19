package com.exercise.bop;

/**
 * ����������У���������A
 * ��̬�滮���������״̬���飺LIS��MAX
 * MAX[LIS[i]]��LIS[i]Ϊ��a[i]���Ԫ�ص�����������г��ȣ�MAX[LIS[i]]Ϊʹ�ó���ΪLIS[i]�ĵ��������е����Ԫ�ص���Сֵ��
 * ��˼�ǿ����ж�����������г���ΪLIS[i]��������Ҫ�������������Ԫ��Ҳ�������һ��Ԫ�أ��ú����Ԫ�ؽ��бȽϣ�����������Ԫ��
 * ˵�����Լ���������������У���������Ӧ���������г���ΪLIS[i]���������һ��Ԫ�ص���Сֵ��
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
                //���ڵ�ǰ����lis
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
