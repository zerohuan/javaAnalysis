package com.exercise.standard;

/**
 * 机器人的运动范围
 * 地上有一个m行和n列的方格。一个机器人从坐标0,0的格子开始移动，每一次只能向左，右，上，下四个方向移动一格，
 * 但是不能进入行坐标和列坐标的数位之和大于k的格子。 例如，当k为18时，机器人能够进入方格（35,37），因为3+5+3+7 = 18。
 * 但是，它不能进入方格（35,38），因为3+5+3+8 = 19。请问该机器人能够达到多少个格子？
 *
 * 解法：
 * 回溯法；
 *
 *
 * Created by yjh on 16-2-5.
 */
public class Ex_67_MovingCount {
    public static int movingCount(int threshold, int rows, int cols) {
        if (threshold >= 0 && rows >= 0 || cols >= 0) {
            return movingC(threshold, 0, 0, rows, cols, new boolean[rows * cols]);
        }
        return -1;
    }
    private static int movingC(int threshold, int i, int j, int rows, int cols, boolean[] counted) {
        int count = 0;

        if (i >= 0 && j >= 0 && i < rows && j < cols
                && check(threshold, i, j) && !counted[i * cols + j]) {
            counted[i * cols + j] = true;
            count += 1 + movingC(threshold, i - 1, j, rows, cols, counted)
                    + movingC(threshold, i + 1, j, rows, cols, counted)
                    + movingC(threshold, i, j - 1, rows, cols, counted)
                    + movingC(threshold, i, j + 1, rows, cols, counted);
        }

        return count;
    }
    private static boolean check(int threshold, int i, int j) {
        int count = 0;
        while (i > 0) {
            count += i % 10;
            i /= 10;
        }
        while (j > 0) {
            count += j % 10;
            j /= 10;
        }
        return count <= threshold;
    }

    public static void main(String[] args) {
        System.out.println(movingCount(5,10,10));
    }
}
