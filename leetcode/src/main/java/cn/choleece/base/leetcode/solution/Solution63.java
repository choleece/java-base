package cn.choleece.base.leetcode.solution;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-01-30 13:09
 **/
public class Solution63 {
    public static int uniquePathsWithObstacles(int[][] obstacleGrid) {
        if (obstacleGrid == null) {
            return 0;
        }

        int x = obstacleGrid.length;
        int y = obstacleGrid[0].length;

        // 记录以往结果
        Integer[][] pathes = new Integer[x][y];

        // 从小到大开始计算
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {

                // 构造状态转移公式
                if (obstacleGrid[i][j] == 1) {
                    pathes[i][j] = 0;
                } else {
                    pathes[i][j] = (i == 0 && i == j)
                            ? 1 : (i == 0) ? pathes[i][j - 1] :
                            (j == 0) ? pathes[i - 1][j] : pathes[i - 1][j] + pathes[i][j - 1];

                }
            }

        }

        return pathes[x - 1][y - 1];
    }

    public static int uniquePathsWithObstacles1(int[][] obstacleGrid) {
        if (obstacleGrid == null || obstacleGrid.length == 0) {
            return 0;
        }

        int x = obstacleGrid.length;
        int y = obstacleGrid[0].length;

        for (int i = x - 1; i >= 0; i--) {
            for (int j = y - 1; j >= 0; j--) {
                // 从最后一个开始，往前逆推
                if (obstacleGrid[i][j] == 1) {
                    obstacleGrid[i][j] = 0;
                } else if (i == x - 1 && j == y - 1) {
                    obstacleGrid[i][j] = 1;
                } else {
                    if (i < x - 1) {
                        obstacleGrid[i][j] += obstacleGrid[i + 1][j];
                    }

                    if (j < y - 1) {
                        obstacleGrid[i][j] += obstacleGrid[i][j + 1];
                    }
                }
            }
        }

        return obstacleGrid[0][0];
    }

    public static void main(String[] args) {
        int[][] obstacleGrid = {
                {0, 0, 0},
                {0, 1, 0},
                {0, 0, 0}
        };

        System.out.println("result: " + uniquePathsWithObstacles(obstacleGrid));

        System.out.println("result1: " + uniquePathsWithObstacles1(obstacleGrid));
    }
}
