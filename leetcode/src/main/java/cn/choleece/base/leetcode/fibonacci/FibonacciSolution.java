package cn.choleece.base.leetcode.fibonacci;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-01-29 15:25
 **/
public class FibonacciSolution {

    /**
     * 斐波拉契递归算法
     * @param n
     * @return
     */
    public static int fibonacciRecursion(int n) {
        // recursion termination 终止条件
        if (n == 1 || n == 2) {
            return 1;
        }

        // drill down 重复调用
        return fibonacciRecursion(n - 1) + fibonacciRecursion(n - 2);
    }

    /**
     * 斐波拉契动态规划算法
     * @param n
     * @return
     */
    public static int fibonacciDynamic(int n) {
        // 用于存储每一步的结果
        int[] result = new int[n + 1];

        // 从小到达开始计算
        for (int i = 1; i <= n; i++) {
            if (i < 2) {
                result[i] = i;
            } else {
                // 状态转移方程
                result[i] = result[i - 1] + result[i - 2];
            }
        }

        return result[n];
    }

    public static void main(String[] args) {
        System.out.println("recursion result: " + fibonacciRecursion(24));
        System.out.println("dynamic result: " + fibonacciDynamic(24));
    }
}
