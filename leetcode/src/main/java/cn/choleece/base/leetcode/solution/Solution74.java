package cn.choleece.base.leetcode.solution;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-01-31 00:15
 **/
public class Solution74 {
    public static boolean searchMatrix(int[][] matrix, int target) {
        if (matrix == null || matrix.length == 0) {
            return false;
        }

        int x = matrix.length;
        int y = matrix[0].length;

        int left = 0;
        int right = x * y - 1;

        while (left <= right) {

            int mid = (left + right) / 2;
            int xMid = mid / y;
            int yMid = mid % y;

            int midVal = matrix[xMid][yMid];
            if (midVal == target) {
                return true;
            } else if (midVal > target) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        return false;
    }

    public static void main(String[] args) {
        int[][] matrix = {
                {1,   3,  5,  7},
                {10, 11, 16, 20},
                {23, 30, 34, 50}
        };

        System.out.println("result: " + searchMatrix(matrix, 51));
    }
}
