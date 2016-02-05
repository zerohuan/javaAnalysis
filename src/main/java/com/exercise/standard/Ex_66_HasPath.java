package com.exercise.standard;

/**
 * 矩阵中的路径
 * 请设计一个函数，用来判断在一个矩阵中是否存在一条包含某字符串所有字符的路径。
 * 路径可以从矩阵中的任意一个格子开始，每一步可以在矩阵中向左，向右，向上，向下移动一个格子。
 * 如果一条路径经过了矩阵中的某一个格子，则该路径不能再进入该格子。
 * 例如 a b c e s f c s a d e e 矩阵中包含一条字符串"bcced"的路径，但是矩阵中不包含"abcb"路径，
 * 因为字符串的第一个字符b占据了矩阵中的第一行第二个格子之后，路径不能再次进入该格子。
 *
 * a b c e
 * s f c s
 * a d e e
 *
 * 解法：
 *
 * 注意：
 * 利用短路
 *
 * Created by yjh on 16-2-5.
 */
public class Ex_66_HasPath {
    public static boolean hasPath(char[] matrix, int rows, int cols, char[] str) {
        if (matrix != null && matrix.length >= 0) {
            boolean[] visited = new boolean[matrix.length];

            for (int i = 0; i < rows; ++i) {
                for (int j = 0; j < cols; ++j) {
                    if (hasP(matrix, i, j, rows, cols, 0, str, visited))
                        return true;
                }
            }
        }
        return false;
    }

    private static boolean hasP(char[] matrix, int i, int j, int rows, int cols, int l, char[] str, boolean[] visited) {
        boolean flag;
        if (l < str.length) {
            if (i >= 0 && j >= 0 && i < rows && j < cols
                    && matrix[i * cols + j] == str[l]
                    && !visited[i * cols + j]) {
                l++;
                visited[i * cols + j] = true;
                flag = hasP(matrix, i + 1, j, rows, cols, l, str, visited)
                        || hasP(matrix, i - 1, j, rows, cols, l, str, visited)
                        || hasP(matrix, i, j + 1, rows, cols, l, str, visited)
                        || hasP(matrix, i, j - 1, rows, cols, l, str, visited);
                if (!flag)
                    visited[i * cols + j] = false; //复位
            } else
                flag = false;
        } else {
            flag = true;
        }

        return flag;
    }

    public static void main(String[] args) {
        System.out.println("ABCEHJIGSFCSLOPQADEEMNOEADIDEJFMVCEIFGGS".length());
        System.out.println(hasPath("ABCEHJIGSFCSLOPQADEEMNOEADIDEJFMVCEIFGGS".toCharArray(),5,8,"SGGFIECVAASABCEHJIGQEM"
                .toCharArray()));
    }
}
