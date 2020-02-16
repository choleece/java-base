package cn.choleece.base.leetcode.solution.arrays;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-01-31 12:58
 **/
public class Solution81 {

    public static boolean search(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return false;
        }

        int turnPointIdx = 0;
        for (; turnPointIdx < nums.length - 1; turnPointIdx++) {
            if (nums[turnPointIdx] > nums[turnPointIdx + 1]) {
                break;
            }
        }

        int[] nums1 = new int[turnPointIdx + 1];
        int[] nums2 = new int[nums.length - turnPointIdx - 1];

        for (int i = 0; i < nums.length; i++) {
            if (i <= turnPointIdx) {
                nums1[i] = nums[i];
            } else {
                nums2[i - turnPointIdx - 1] = nums[i];
            }
        }

        return binarySearch(nums1, target) || binarySearch(nums2, target);
    }

    public static boolean binarySearch(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return false;
        }

        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = (left + right) / 2;

            if (nums[mid] == target) {
                return true;
            } else if (target > nums[mid]) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return false;
    }

    public static boolean search1(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return false;
        }

        int left = 0;
        int right = nums.length - 1;

        while (left <= right) {
            int mid = (left + right) / 2;
            int midVal = nums[mid];

            if (midVal == target || nums[left] == target || nums[right] == target) {
                return true;
            }

            // 左侧有序
            if (nums[left] < midVal) {
                if (midVal > target && nums[left] <= target) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            } else if (nums[right] > midVal) {
                // 右侧有序
                if (midVal < target && nums[right] >= target) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            } else {
                left++;
                right--;
            }
        }

        return false;
    }

    public static void main(String[] args) {
        int[] nums = {1, 1, 3, 1};

        System.out.println("result: " + search1(nums, 3));
    }
}
