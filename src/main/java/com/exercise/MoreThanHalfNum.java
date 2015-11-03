package com.exercise;

/**
 * 提高时间效率
 *
 * 思路：
 * 因为如果存在超过数组长度一半次数的数，它比其他所有数出现次数之和还要大。
 * （1）因此，可以在遍历数组时保存两个值：一个是数组中的数字，一个是次数；
 * （2）如果下次遍历到的数字和保存的数字相同，次数+1，不同则-1；
 * （3）如果次数为0,保存下一次的数字，并将次数置为1；
 * （4）如果存在目标数字，一定是最后一个设置次数为1的数字；
 *
 * 注意：
 * （1）需要检查正确性；
 *
 * Created by yjh on 15-11-3.
 */
public class MoreThanHalfNum {
    public static int MoreThanHalfNum_Solution(int [] array) {
        if(!checkInput(array))
            return 0;
        int num = array[0];
        int count = 1;
        int i = 1;
        while(i < array.length) {
            if(count == 0) {
                num = array[i];
                count = 1;
            } else if(array[i] == num)
                count++;
            else {
                count--;
            }
            i++;
        }

        return checkMoreThanHalf(array, num) ? num : 0;
    }

    private static boolean checkInput(int[] array) {
        return !(array == null || array.length == 0);
    }

    private static boolean checkMoreThanHalf(int[] array, int num) {
        int count = 0;
        for(int n : array) {
            if(n == num)
                count++;
        }
        return count > (array.length >> 1);
    }

    public static void main(String[] args) {
        int[] num = {1,2,3,2,2,2,5,4,2};
        System.out.println(MoreThanHalfNum_Solution(num));
    }
}
