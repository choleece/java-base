package cn.choleece.base.leetcode.solution.arrays;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-01-31 14:46
 **/
public class Solution153 {

    public static int findMin(int[] nums) {
        if (nums == null || nums.length == 0) {
            return Integer.MIN_VALUE;
        }

        int left = 0;
        int right = nums.length - 1;

        int min = Integer.MIN_VALUE;

        while (left <= right) {

            if (nums[right] >= nums[left]) {
                return nums[left];
            }

            int mid = (left + right) / 2;
            int midVal = nums[mid];

            if (mid - 1 >= left && mid + 1 <= right && nums[mid - 1] > midVal && nums[mid + 1] > midVal) {
                return midVal;
            }

            if (midVal > nums[right]) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return min;
    }

    public static void main(String[] args) {
        int[] nums = {4, 5, 1, 2, 3};

        System.out.println("min result: " + findMin(nums));
    }
}
