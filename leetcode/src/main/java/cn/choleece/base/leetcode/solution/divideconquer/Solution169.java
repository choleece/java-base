package cn.choleece.base.leetcode.solution.divideconquer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-01-31 19:25
 **/
public class Solution169 {

    public static int majorityElement(int[] nums) {

        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {

            Integer times = map.get(nums[i]);
            if (times == null) {
                map.put(nums[i], 1);
            } else {
                if (times + 1 > nums.length / 2) {
                    return nums[i];
                }

                map.put(nums[i], times + 1);
            }
        }

        return nums[0];
    }

    public static int majorityElement1(int[] nums) {

        int majority = nums[0];
        int count = 1;

        // 走到最后count不为0， 那么一定是 > n / 2的，也就是众数
        for (int i = 1; i < nums.length; i++) {
            if (count == 0) {
                majority = nums[i];
                count++;
            } else if (majority == nums[i]) {
                count++;
            } else {
                count--;
            }
        }

        return majority;
    }

    public static int majorityElement2(int[] nums) {

        int left = 0;
        int right = nums.length - 1;

        return majorityElementDivideConquer(nums, left, right);
    }

    public static int majorityElementDivideConquer(int[] nums, int left, int right) {
        // recursion termination
        if (left == right) {
            return nums[left];
        }

        // process data
        int mid = (left + right) / 2;

        // sub problems
        int leftMajorityVal = majorityElementDivideConquer(nums, left, mid);
        // sub problems
        int rightMajorityVal = majorityElementDivideConquer(nums, mid + 1, right);

        // process result
        if (leftMajorityVal == rightMajorityVal) {
            return leftMajorityVal;
        }

        int leftCount = countSpecEle(nums, leftMajorityVal, left, right);
        int rightCount = countSpecEle(nums, rightMajorityVal, left, right);

        return leftCount > rightCount ? leftMajorityVal : rightMajorityVal;
    }

    public static int countSpecEle(int[] nums, int num, int left, int right) {
        int count = 0;
        for (int i = left; i <= right ; i++) {
            if (nums[i] == num) {
                count++;
            }
        }

        return count;
    }

    public static void main(String[] args) {
        int[] nums = {2, 2, 1, 1, 1, 2, 2};

        System.out.println("result: the majority element is " + majorityElement(nums));

        System.out.println("result1: the majority element is " + majorityElement1(nums));

        System.out.println("result2: the majority element is " + majorityElement2(nums));
    }

}
