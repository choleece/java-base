package cn.choleece.base.leetcode.solution.arrays;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-01-28 22:33
 **/
public class Solution1 {

    public static int[] addTwoSum(int[] nums, int target) {
        if (nums == null || nums.length < 2) {
           return null;
        }

        Map<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            if (!map.containsKey(target - nums[i])) {
                map.put(nums[i], i);
            } else {
                System.out.println("result1 i: " + i + " j: " + map.get(target - nums[i]));
                return new int[] {i, map.get(target - nums[i])};
            }
        }

        return null;
    }

    /**
     * 这一种解法有问题，会出现位置排序后不对对问题，此方法只适用于求解有哪些值，不适合求索引位置
     * @param nums
     * @param target
     * @return
     */
    public static int[] addTwoSum1(int[] nums, int target) {
        if (nums == null || nums.length < 2) {
            return null;
        }

        // 先对数组进行排序，然后从收尾开始进行
        Arrays.sort(nums);

        int i = 0, j = nums.length - 1;

        while (i <= j) {
            int sum = nums[i] + nums[j];
            if (sum == target) {
                System.out.println("result1 i: " + i + " j: " + j);
                return new int[] {i, j};
            } else if (sum > target) {
                j--;
            } else {
                i++;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        int[] nums = {1, 4, 7, 5, 10, 2};

        addTwoSum(nums, 11);
        addTwoSum1(nums, 11);
    }
}
