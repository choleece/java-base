package cn.choleece.base.leetcode.code53;

/**
 * Given an integer array nums, find the contiguous subarray (containing at least one number) which has the largest sum and return its sum.
 */
public class Solution53 {

    public static int maxSubArray(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int maxSum = Integer.MIN_VALUE;
        int cur = 0;
        for (int i = 0; i < nums.length; i++) {
            cur += nums[i];
            maxSum = Math.max(maxSum, cur);
            if (cur < 0) {
                cur = 0;
            }
        }
        return maxSum;
    }

    public static void main(String[] args) {
        int[] nums = {-2, -1, -3, -4, -1, -2, -2, -5, -4};
        System.out.println("max sum: " + maxSubArray(nums));
    }
}
