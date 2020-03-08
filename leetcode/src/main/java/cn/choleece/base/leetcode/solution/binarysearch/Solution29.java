package cn.choleece.base.leetcode.solution.binarysearch;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-03-08 22:06
 **/
public class Solution29 {
    public static int divide(int dividend, int divisor) {
        int a = 0, b = 0;
        if (dividend < 0) {
            dividend = 0 - dividend;
            a = -1;
        }
        if (divisor < 0) {
            divisor = 0 - divisor;
            b = -1;
        }

        int res = positiveDivide(dividend, divisor);

        return (a + b == -1) ? 0 - res : res;
    }

    public static int positiveDivide(int dividend, int divisor) {
        if (dividend < divisor) {
            return 0;
        }

        if (dividend == divisor) {
            return 1;
        }

        if (divisor == 1) {
            return dividend;
        }

        int i = 0;
        while (dividend - divisor > 0) {
            i++;
            dividend -= divisor;
        }

        return i;
    }

    public static void main(String[] args) {
        System.out.println("result: " + divide(-2147483647, -1));
    }
}
