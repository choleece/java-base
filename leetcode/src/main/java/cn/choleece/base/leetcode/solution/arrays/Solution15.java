package cn.choleece.base.leetcode.solution.arrays;

import java.util.*;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-03-08 17:23
 **/
public class Solution15 {

    public static List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new LinkedList<>();
        if (nums == null || nums.length < 3) {
            return result;
        }

        // 先对数组进行排序, 然后在解决add two sum对基础上去进行求解
        Arrays.sort(nums);

        for (int i = 0; i < nums.length - 1; i++) {
            addTwoSum(nums[i], i + 1, nums, result);
        }

        return result;
    }

    private static List<Integer> addTwoSum(int target, int i, int[] nums, List<List<Integer>> result) {
        if (i >= nums.length - 1) {
            return null;
        }

        int n = nums.length - 1;

        while (i < n) {
            int sum = nums[i] + nums[n];

            if (sum + target == 0) {
                 addResult(result, new LinkedList<Integer>(Arrays.asList(target, nums[i], nums[n])));
                 i++;
            } else if (sum + target > 0) {
                if (target <= 0) {
                    n--;
                } else {
                    i++;
                }
            } else {
                if (target <= 0) {
                    i++;
                } else {
                    n--;
                }
            }
        }

        return null;
    }

    static Set<String> RESULT_SET = new HashSet<>();

    // 结果能算出来，不知为何leetcode 上跑的结果不对
    public static void addResult(List<List<Integer>> result, List<Integer> res) {
        String key = new StringBuffer(String.valueOf(res.get(0)))
                .append(String.valueOf(res.get(1))).append(String.valueOf(res.get(2))).toString();

        if (!RESULT_SET.contains(key)) {
            result.add(res);
            RESULT_SET.add(key);
        }
    }

    public static void main(String[] args) {
        int[] nums = {-1, 0, 1};

        System.out.println("result: " + threeSum(nums));
    }
}
