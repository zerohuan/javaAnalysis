package com.exercise.standard;

/**
 * Created by yjh on 16-2-26.
 */
public class Ex_3_FindInTwoDimensionalArray {
    public static boolean find(int[][] arr, int num) {
        boolean res = false;
        if (arr != null && arr.length != 0 && arr[0].length != 0 ) {
            int rows = 0;
            int cols = arr[0].length - 1;
            while (rows < arr.length && cols >= 0) {
                if (arr[rows][cols] == num) {
                    res = true;
                    break;
                } else if (arr[rows][cols] > num)
                    cols--;
                else
                    rows++;
            }
        }
        return res;
    }

    public static void main(String[] args) {

    }
}
