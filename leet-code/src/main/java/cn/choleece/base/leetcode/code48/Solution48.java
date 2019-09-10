package cn.choleece.base.leetcode.code48;

/**
 * You are given an n x n 2D matrix representing an image.
 *
 * Rotate the image by 90 degrees (clockwise).
 */
public class Solution48 {

    public static void rotate(int[][] matrix) {
        if (matrix == null || matrix.length <= 1) {
            return;
        }

        int n = matrix.length - 1;

        for (int x = 0; x < matrix.length / 2.0; x++) {
            for (int y = 0; y < matrix.length; y++) {
                int val = matrix[x][y];
                matrix[x][y] = matrix[n - y][x];
                matrix[n - y][x] = matrix[n - x][n - y];
                matrix[n - x][n - y] = matrix[y][n - x];
                matrix[y][n - x] = val;
            }
        }
    }

    public static void main(String[] args) {
        int[][] matrix = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };

        printMatrix(matrix);

        rotate(matrix);

        printMatrix(matrix);
    }

    public static void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println("");
        }
    }

}
