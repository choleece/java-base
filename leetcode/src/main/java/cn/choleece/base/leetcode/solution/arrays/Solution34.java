package cn.choleece.base.leetcode.solution.arrays;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-01-30 16:53
 **/
public class Solution34 {
    public static int[] searchRange(int[] nums, int target) {
        int[] result = {-1, -1};

        if (nums == null || nums.length == 0) {
            return result;
        }

        int left = 0;
        int right = nums.length - 1;

        while (left <= right) {
            int mid = (left + right) / 2;

            if (target == nums[mid]) {
                int m = mid;

                while (m >= 0 && nums[m] == target) {
                    result[0] = m;
                    m--;
                }

                while (mid <= right && nums[mid] == target) {
                    result[1] = mid;
                    mid++;
                }

                return result;
            } else if (target > nums[mid]) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return result;
    }

    public static void main(String[] args) {
        int[] nums = {5, 6, 7, 7, 8, 8, 10};

        int[] res = searchRange(nums, 6);
        System.out.println("result: 0 idx: " + res[0] + " , 1 idx: " + res[1]);
    }
}
