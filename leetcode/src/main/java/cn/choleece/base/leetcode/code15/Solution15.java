package cn.choleece.base.leetcode.code15;

import java.util.*;

/**
 * Given an array nums of n integers, are there elements a, b, c in nums such that a + b + c = 0? Find all unique triplets in the array which gives the sum of zero
 */
public class Solution15 {

    public static List<List<Integer>> threeSum1(int[] nums) {

        List<List<Integer>> solution = new LinkedList<List<Integer>>();

        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                for (int k = j + 1; k < nums.length; k++) {
                    if (nums[i] + nums[j] + nums[k] == 0) {
                        solution.add(Arrays.asList(nums[i], nums[j], nums[k]));
                    }
                }
            }
        }

        return solution;
    }

    public static List<List<Integer>> threeSum(int[] nums) {

        List<List<Integer>> solution = new LinkedList<List<Integer>>();

        for (int i = 0; i < nums.length; i++) {
            // 取另外两个数的和
            int gap = 0 - nums[i];
            Map<Integer, Integer> map = new HashMap<Integer, Integer>(nums.length);
            for (int j = i + 1; j < nums.length; j++) {
                if (map.containsKey(gap - nums[j])) {
                    List<Integer> item = Arrays.asList(nums[i], nums[map.get(gap - nums[j])], nums[j]);
                    // remove duplicate solution
                    if (!hasTriplets(solution, item)) {
                        solution.add(item);
                    }
                } else {
                    map.put(nums[j], j);
                }
            }
        }

        return solution;
    }

    public static boolean hasTriplets(List<List<Integer>> solution, List<Integer> list) {
        for (List<Integer> item : solution) {
            if (item.contains(list.get(0)) && item.contains(list.get(1)) && item.contains(list.get(2))
                    && list.contains(item.get(0)) && list.contains(item.get(1)) && list.contains(item.get(2))) {
                return true;
            }
        }

        return false;
    }

    public static void main(String[] args) {
        int[] nums = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

        System.out.println(threeSum(nums));
    }

}
