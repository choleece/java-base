package cn.choleece.base.leetcode.solution;

import java.util.HashMap;
import java.util.Map;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-01-28 22:33
 **/
public class Solution1 {

    public int[] addTwoSum(int[] nums, int target) {
        if (nums == null || nums.length < 2) {
           return null;
        }

        Map<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            if (!map.containsKey(target - nums[i])) {
                map.put(nums[i], i);
            } else {
                return new int[] {i, map.get(target - nums[i])};
            }
        }

        return null;
    }
}
