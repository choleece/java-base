package cn.choleece.base.leetcode.solution.arrays;

/**
 * @author choleece
 * @Description: 利用动态规划，三步骤
 * @Date 2020-01-29 22:24
 **/
public class Solution62 {

    public static int uniquePaths(int m, int n) {

        if (m <= 0 || n <= 0) {
            return 0;
        }

        // 存储以往历史结果
        Integer[][] paths = new Integer[m][n];

        // 从小开始进行
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // 进行状态转移方程: path(n)(m) = path(n)f(m - 1) + path(n - 1)(m)
                paths[i][j] = (i == 0 || j == 0) ? 1 : paths[i][j - 1] + paths[i - 1][j];
            }
        }

        return paths[m - 1][n - 1];
    }

    public static int uniquePathsWithRecursion(int m, int n) {

        if (m < 0 || n < 0) {
            return 0;
        }

        if (m == 0 || n == 0) {
            return 1;
        }
        return uniquePathsWithRecursion(m, n - 1) + uniquePathsWithRecursion(m - 1, n);
    }

    public static void main(String[] args) {
        System.out.println("total paths: " + uniquePaths(1, 1));

        System.out.println("total paths with recursion: " + uniquePathsWithRecursion(1, 1));
    }
}
