package cn.choleece.base.leetcode.code50;

/**
 * @author choleece
 * @Description: 实现 pow(x, n) ，即计算 x 的 n 次幂函数。
 * @Date 2019-10-31 22:54
 **/
public class Solution55 {

    public static double myPow1(double x, int n) {
        return Math.pow(x, n);
    }

    public static double myPow2(double x, int n) {
        if (n == 0) {
            return 1;
        }

        if (n < 0) {
            return 1.0 / (x * myPow2(x, -n - 1));
        }
        return x * myPow2(x, n - 1);
    }

    /**
     * 将问题拆解 注意n 为负数的情况，需要倒置， 其他的情况可以根据n为奇偶，如果 n 为奇数， fn = x * pow(x, n / 2), 这里n / 2取整数，如果是偶数，那么可以直接pow(x * x, n / 2)
     * @param x
     * @param n
     * @return
     */
    public static double myPow3(double x, int n) {

        if (n > Integer.MAX_VALUE || n <= Integer.MIN_VALUE) {
            throw new IllegalArgumentException("不合法的参数");
        }

        if (n == 0) {
            return 1;
        }

        if (n < 0) {
            return 1.0 / myPow3(x, -n);
        }

        if (n % 2 == 1) {
            return x * myPow3(x, n - 1);
        }

        return myPow3(x * x, n / 2);
    }

    public static void main(String[] args) {
        System.out.println("my pow1: " + myPow1(10, -2));

        System.out.println("my pow2: " + myPow2(10, -2));

        System.out.println(Integer.MIN_VALUE);

        System.out.println("my pow3: " + myPow3(2.00000, Integer.MIN_VALUE + 1));
    }
}
