package com.exercise;

import java.util.ArrayList;

/**
 *
 *
 * Created by yjh on 15-11-11.
 */
public class MatrixPrint {
    //[[1],[2],[3],[4],[5]]
    public static ArrayList<Integer> printMatrix(int [][] matrix) {
        ArrayList<Integer> res = new ArrayList<Integer>();
        if(matrix == null || matrix.length == 0)
            return res;

        int row = matrix.length;
        int col = matrix[0].length;

        for(int start = 0; row > start << 1 && col > start << 1; ++start) {
            //顺时针打印一圈
            //打印上面一行
            for(int i = start; i < col - start; i++)
                res.add(matrix[start][i]);
            //打印右边一列
            for(int i = start + 1; i < row - start; i++)
                res.add(matrix[i][col - start - 1]);
            //打印下面一行
            for(int i = col - start - 2; i >= start && start != row - start - 1; --i) {
                res.add(matrix[row - start - 1][i]);
            }
            //打印左边一列
            for(int i = row - start - 2; i > start && start != col - start - 1; --i) {
                res.add(matrix[i][start]);
            }
        }
        return res;
    }

    public static void main(String[] args) {
        int[][] matrix = {
                {1,2,3,4,5},
                {6,7,8,9,10},
                {11,12,13,14,15},
                {16,17,18,19,20},
                {21,22,23,24,25}
        };
//        System.out.println(printMatrix(matrix));
        int[][] matrix2 = {
                {1,2},
                {3,4}
        };
        System.out.println(printMatrix(matrix2));
    }
}
