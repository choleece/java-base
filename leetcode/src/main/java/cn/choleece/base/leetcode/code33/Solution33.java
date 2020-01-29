package cn.choleece.base.leetcode.code33;

/**
 * @author choleece
 * @Description: Suppose an array sorted in ascending order is rotated at some pivot unknown to you beforehand.
 *
 * (i.e., [0,1,2,4,5,6,7] might become [4,5,6,7,0,1,2]).
 *
 * You are given a target value to search. If found in the array return its index, otherwise return -1.
 *
 * You may assume no duplicate exists in the array.
 *
 * Your algorithm's runtime complexity must be in the order of O(log n).
 * @Date 2019-09-28 23:34
 **/
public class Solution33 {

    public static int search(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return -1;
        }

        int left = 0, right = nums.length - 1;

        while (left <= right) {
            int mid = (left + right) / 2;

            if (nums[mid] == target) {
                return mid;
            }


        }

        return -1;
    }

    public static void main(String[] args) {
        int[] nums = {4,5,6,7,8,1,2,3 };

        System.out.println(search(nums, 8));
    }
}
