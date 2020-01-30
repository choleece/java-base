package cn.choleece.base.leetcode.solution;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-01-30 14:26
 **/
public class Solution70 {
    public static int climbStairs(int n) {
        if (n <= 0) {
            return 0;
        }

        // 保存历史结果
        Integer[] pathes = new Integer[n + 1];
        pathes[0] = 1;
        pathes[1] = 1;

        // 从小到大开始执行
        for (int i = 2; i <= n ; i++) {

            // 状态转移公式 path(n) = path(n - 1) + path(n - 2)
            pathes[i] = pathes[i - 1] + pathes[i -2];
        }

        return pathes[n];
    }

    public static int climbStairsWithRecursion(int n) {
        if (n <= 0) {
            return 0;
        }

        // 保存历史结果
        Integer[] pathes = new Integer[n + 1];

        return helper(n, pathes);
    }

    public static int helper(int n, Integer[] pathes) {
       if (n == 0) {
           return 1;
       } else if (n < 0) {
           return 0;
       } else if (pathes[n] != null) {
           return pathes[n];
       } else {
           // 状态转移公式 path(n) = path(n - 1) + path(n - 2)
           return pathes[n] = helper(n - 1, pathes) + helper(n - 2, pathes);
       }
    }

    public static void main(String[] args) {
        System.out.println("result: " + climbStairs(4));

        System.out.println("recursion result: " + climbStairsWithRecursion(4));
    }
}
