package cn.choleece.base.leetcode.code54;

import java.util.LinkedList;
import java.util.List;

/**
 * Given a matrix of m x n elements (m rows, n columns), return all elements of the matrix in spiral order.
 * 遍历二维数组
 * 思路：一层一层遍历，找好边界条件
 * @author choleece
 */
public class Solution54 {

    public static List<Integer> spiralOrder1(int[][] matrix) {
        List<Integer> list = new LinkedList<Integer>();

        if (matrix == null || matrix.length == 0) {
            return list;
        }

        int rows = matrix.length;
        int columns = matrix[0].length;
        int length = rows * columns;
        int cycle = 0, x = cycle, y = cycle;

        for (int i = 0; i < length; i++) {

            System.out.println("x: " + x + " y: " + y);
            list.add(matrix[x][y]);

            // go right
            if (x == cycle && y < columns - cycle - 1) {
                y++;
            } else if (y == columns - 1 - cycle && x >= cycle && x < rows - cycle - 1) {
                // go down
                x++;
            } else if (x == rows - cycle - 1 && y <= columns - 1 - cycle && y > cycle) {
                // go left
                y--;
            } else if (y == cycle && x <= rows - cycle - 1) {
                // go up
                x--;
            }

            // next cycle
            if (x == cycle && y == cycle) {
                cycle++;
                x = cycle;
                y = cycle;
            }
        }
        return list;
    }

    public static List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> list = new LinkedList<Integer>();

        if (matrix == null || matrix.length == 0) {
            return list;
        }

        int rows = matrix.length, columns = matrix[0].length;
        int top = 0, right = columns - 1, bottom = rows - 1, left = 0;
        while (list.size() < rows * columns) {
            // list top
            for (int i = left; i <= right; i++) {
                list.add(matrix[top][i]);
            }
            // list right
            for (int i = top + 1; i <= bottom; i++) {
                list.add(matrix[i][right]);
            }
            // list bottom
            for (int i = right - 1; i >= left && list.size() < rows * columns; i--) {
                list.add(matrix[bottom][i]);
            }
            // list left
            for (int i = bottom - 1; i > top && list.size() < rows * columns; i--) {
                list.add(matrix[i][left]);
            }
            top++;
            right--;
            bottom--;
            left++;
        }

        return list;
    }



    public static void main(String[] args) {
        int[][] matrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        List<Integer> list = spiralOrder(matrix);

        System.out.println(list);
    }
    
}
