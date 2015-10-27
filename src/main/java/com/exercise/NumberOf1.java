package com.exercise;

/**
 * 输入一个整数，输出该数二进制表示中1的个数。其中负数用补码表示。
 *
 * 注意：Java中的位符号：
 * （1）<<：向左位移；
 * （2）>>：有符号的向右位移，负数实在高位补1,因此如果用>>的方式统计1的个数要注意死循环；
 * （3）>>>：无论正负都在高位补0,Java有这个运算符，C++没有；
 * （4）～：符号位也取反，但是负数取反后不会+1,例如~(-9)=8；
 * （5）-：正负转换，负数取反会+1；
 * （6）Integer.toBinaryString()的应用；
 *
 * 思路：
 * 方法一：>>>，n!=0判断；
 * 方法二：用一个flag=1，flag&n，然后不断flag<<1直到flag==0；我觉得这个方法是最好的；
 * 方法三：如下面的实现，n=(n-1)&n是关键；这个方法在计算-1和Integer.MAX_VALUE时开销较大；
 *
 * Created by yjh on 15-10-27.
 */
public class NumberOf1 {
    public int NumberOf1(int n) {
        int count = 0;
        int flag = 1;
        while (flag != 0) {
            if((flag & n) != 0)
                count++;
            flag <<= 1;
        }
        return count;
    }

    public static void main(String[] args) {
        NumberOf1 numberOf1 = new NumberOf1();
        System.out.println(numberOf1.NumberOf1(-1));
        byte b = -1;
        b >>>= 10;
        b = -1;
        System.out.println(Integer.toBinaryString(Integer.MIN_VALUE + 2));
        System.out.println(Integer.toBinaryString(Integer.MIN_VALUE + 1));
    }
}
