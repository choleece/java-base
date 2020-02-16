package cn.choleece.base.leetcode.solution.arrays;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-01-30 15:26
 **/
public class Solution33 {
    public static int search(int[] nums, int target) {
        if (nums == null || nums.length < 1) {
            return -1;
        }

        int left = 0;
        int right = nums.length - 1;

        while (left <= right) {
            int mid = (left + right) / 2;

            if (target == nums[mid]) {
                return mid;
            }

            // mid左侧有序
            if (nums[left] < nums[mid]) {
               if (target >= nums[left] && target < nums[mid]) {
                   right = mid - 1;
               } else {
                   left = mid + 1;
               }
            } else {
                if (mid + 1 <= right) {
                    if (target >= nums[mid + 1] && target <= nums[right]) {
                        left = mid + 1;
                    } else {
                        right = mid - 1;
                    }
                } else {
                    right = mid - 1;
                }
            }
        }

        return -1;
    }

    public static int binarySearch(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;

        while (left <= right) {
            int mid = (left + right) / 2;

            if (target == nums[mid]) {
                return mid;
            } else if (target > nums[mid]) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return -1;

    }

    public static void main(String[] args) {
        int[] nums = {1, 3};

        System.out.println(search(nums, 3));

        int[] orderNums = {1, 2};

        System.out.println("binary search: " + binarySearch(orderNums, 2));
    }
}
