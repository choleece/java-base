package cn.choleece.base.leetcode.solution.greedy;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-02-05 21:46
 **/
public class Solution860 {

    public static boolean lemonadeChange(int[] bills) {
        if (bills == null || bills.length == 0) {
            return true;
        }

        int fiveCount = 0;
        int tenCount = 0;

        for (int i = 0; i < bills.length; i++) {
            if (bills[i] == 5) {
                fiveCount++;
            }

            if (bills[i] == 10) {
                if (fiveCount <= 0) {
                    return false;
                }
                fiveCount--;
                tenCount++;
            }

            if (bills[i] == 20) {

                if (tenCount <= 0) {
                    if (fiveCount < 3) {
                        return false;
                    }
                    fiveCount -= 3;
                } else {
                    if (fiveCount <= 0) {
                        return false;
                    }
                    tenCount--;
                    fiveCount--;
                }
            }
        }

        return true;
    }

    public static void main(String[] args) {
        int[] bills = {5, 10, 10, 5};
        System.out.println("result: " + lemonadeChange(bills));
    }
}
