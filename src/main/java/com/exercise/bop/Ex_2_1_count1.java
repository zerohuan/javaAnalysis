package com.exercise.bop;

/**
 * �����������1�ĸ���
 * Created by yjh on 2016/3/14.
 */
public class Ex_2_1_count1 {
    public static int count1(int p) {
        int count = 0;
        while (p > 0) {
            count += (p & 1);
            p >>= 1;
        }
        return count;
    }

    //ʱ�临�Ӷ�O(M)��MΪ�����Ʊ�ʾ��1�ĸ���
    public static int count1_2(int b) {
        int count = 0;
        while (b > 0) {
            b &= (b - 1);
            count++;
        }
        return count;
    }

    //�ⷨ����Ԥ�ȱ���ÿ���ֽڶ�Ӧ��1�ĸ�����ֱ�ӷ��أ��ռ任ʱ��

    public static void main(String[] args) {
        System.out.println(count1(255));
    }
}
