package cn.choleece.base.leetcode.solution;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-01-31 15:39
 **/
public class Solution162 {
    public static int findPeakElement(int[] nums) {
        if (nums == null || nums.length == 0) {
            return -1;
        }

        int left = 0;
        int right = nums.length - 1;

        int res = helper(nums, left, right);

        if (res == 0) {
            return nums[0] > nums[right] ? 0 : right;
        }
        return res;
    }

    public static int helper(int[] nums, int l, int r) {
        int mid = (l + r) / 2;
        // mid < 1，返回0，说明总元素只有1个或者2个
        // mid > r - 1, 返回0，说明l, r相连
        if (mid < 1 || mid > r - 1) {
            return 0;
        }

        // 大于其左右
        if (nums[mid - 1] < nums[mid] && nums[mid] > nums[mid + 1]) {
            return mid;
        }

        // 如果没找到，在左边，右边取最大值
        return Math.max(helper(nums, l, mid), helper(nums, mid + 1, r));
    }

    public static void main(String[] args) {
        int[] nums = {3, 4, 3, 2, 1};

        System.out.println("result, the peek element idx is : " + findPeakElement(nums));
    }
}
