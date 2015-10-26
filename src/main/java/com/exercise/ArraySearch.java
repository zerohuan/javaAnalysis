package com.exercise;

/**
 * 在一个二维数组中，每一行都按照从左到右递增的顺序排序，
 * 每一列都按照从上到下递增的顺序排序。请完成一个函数，
 * 输入这样的一个二维数组和一个整数，判断数组中是否含有该整数。
 *
 * 思路：
 * （1）比array[0][0]小或者比array[row][col]不再范围内；
 * （2）按对角线向下对列或行进行二分查找：
 * （3）当row>col时，对列进行二分查找；
 * （4）当col>row时，对行进行二分查找；
 *
 * 注意点：
 * （1）二分查找的边界条件:low <= high; low = mid + 1; high = mid - 1; mid = (low + high) / 2；
 * （2）按行和按列的情况划分；
 *
 * Created by yjh on 15-10-25.
 */
public class ArraySearch {
    public boolean Find(int [][] array,int target) {
        int row = array.length - 1, col = array[0].length - 1;
        if(row > col)
            return colSearch(array, target);
        else
            return rowSearch(array, target);
    }

    public boolean rowSearch(int[][] array, int target) {
        int row = array.length - 1, col = array[0].length - 1;
        int low = 0, high = 0, mid = 0;
        for(int i = 0; i <= row; i++) {
            //确定low和row
            if(target < array[i][i]) {
                low = 0;
                high = i;
            } else if(target == array[i][i]) {
                return true;
            } else {
                low = i;
                high = col;
            }

            //二分查找
            while(low <= high) {
                mid = (high + low) >> 1;
                if(array[i][mid] == target) {
                    return true;
                } else if(array[i][mid] > target) {
                    high = mid - 1;
                } else {
                    low = mid + 1;
                }
            }
        }

        return false;
    }

    public boolean colSearch(int[][] array, int target) {
        int row = array.length - 1, col = array[0].length - 1;
        int low = 0, high = 0, mid = 0;
        for(int i = 0; i <= col; i++) {
            //确定low和row
            if(target < array[i][i]) {
                low = 0;
                high = i;
            } else if(target == array[i][i]) {
                return true;
            } else {
                low = i;
                high = row;
            }

            //二分查找
            while(low <= high) {
                mid = (high + low) >> 1;
                if(array[mid][i] == target) {
                    return true;
                } else if(array[mid][i] > target) {
                    high = mid - 1;
                } else {
                    low = mid + 1;
                }
            }
        }

        return false;
    }

    public static void main(String[] args) {
//        int[][] array = {{1,2,3},{4,5,6},{7,8,9},{10,11,12}};
//        System.out.println(Find(array, 6));
    }
}
