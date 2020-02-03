package cn.choleece.base.leetcode.solution;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-02-02 22:22
 **/
public class Solution122 {

    public static int maxProfit(int[] prices) {
        if (prices == null || prices.length == 0) {
            return 0;
        }
        int profit = 0;

        for (int i = 0; i < prices.length - 1; i++) {
            if (prices[i + 1] > prices[i]) {
                profit += (prices[i + 1] - prices[i]);
            }
        }

        return profit;
    }

    public static void main(String[] args) {
        int[] prices = {7, 9};

        System.out.println("max profit: " + maxProfit(prices));
    }

}
