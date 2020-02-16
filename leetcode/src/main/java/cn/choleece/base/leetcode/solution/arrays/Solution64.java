package cn.choleece.base.leetcode.solution.arrays;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-01-30 13:49
 **/
public class Solution64 {

    /**
     * DP
     * @param grid
     * @return
     */
    public static int minPathSum(int[][] grid) {
        if (grid == null || grid.length == 0) {
            return 0;
        }

        int m = grid.length;
        int n = grid[0].length;

        // 记录以往结果
        Integer[][] minPath = new Integer[m][n];

        // 从小到大开始计算
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {

                // 构建状态转移公式 min(m, n) = min(path(m - 1, n), path(m, n - 1)) + grid(m, n)
                if (i == 0 && j == 0) {
                    minPath[i][j] = grid[i][j];
                } else {
                    if (i == 0) {
                        minPath[i][j] = minPath[i][j - 1] + grid[i][j];
                    } else if (j == 0) {
                        minPath[i][j] = minPath[i - 1][j] + grid[i][j];
                    } else {
                        minPath[i][j] = Math.min(minPath[i][j - 1], minPath[i - 1][j]) + grid[i][j];
                    }
                }
            }
        }
        return minPath[m - 1][n - 1];
    }

    public static void main(String[] args) {
        int[][] obstacleGrid = {
                {1, 3, 1},
                {1, 5, 1},
                {4, 2, 1}
        };

        System.out.println("result: " + minPathSum(obstacleGrid));
    }
}
