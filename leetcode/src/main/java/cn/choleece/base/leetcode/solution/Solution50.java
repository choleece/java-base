package cn.choleece.base.leetcode.solution;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-01-30 22:26
 **/
public class Solution50 {

    public static double myPow(double x, int n) {

        if(x == 0) {
            return 0;
        }

        if(n < 0){
            return myPow(1 / x, -(n+1)) / x;
        }

        if(n % 2 == 1){
            return x * myPow(x*x, (n - 1) / 2);
        } else {
            if(n == 0) {
                return 1;
            }
            return myPow(x*x, n / 2);
        }
    }

    public static void main(String[] args) {
        System.out.println("result: " + myPow(2.00000, -1));
    }
}
