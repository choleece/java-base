package cn.choleece.base.leetcode;

import java.util.HashMap;
import java.util.Map;

/**
 * 给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那 两个 整数，并返回他们的数组下标
 */
public class TwoNumberSum01 {

    public int[] twoSum(int[] nums, int target) {
        if (nums == null || nums.length <= 0) {
            throw new IllegalArgumentException("illegal nums");
        }
        if (target > Integer.MAX_VALUE || target < Integer.MIN_VALUE) {
            throw new IllegalArgumentException("illegal target value");
        }
        Map<Integer, Integer> indexMap = new HashMap<>(16);
        int firstIndex = -1, secondIndex = -1;
        for (int i = 0; i < nums.length; i++) {
            int num = nums[i];
            int gapValue = target - num;
            if (indexMap.containsKey(gapValue)) {
                firstIndex = indexMap.get(gapValue);
                secondIndex = i;
                return new int[]{firstIndex, secondIndex};
            } else {
                indexMap.put(num, i);
            }
        }
        throw new IllegalArgumentException("no such two number");
    }

}
