package cn.choleece.base.leetcode.solution.backtrack;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * @description: TODO
 * @author: choleece
 * @time: 2020-02-16 15:08
 */
public class Solution46 {

    public static void backtrack(Stack<Integer> combination, int len, int[] nums,
                                 List<List<Integer>> result, boolean[] used) {

        // if track is ok
        if (combination.size() == len) {
            result.add(new LinkedList<>(combination));
        } else {
            // loop next combination
            for (int i = 0; i < len; i++) {
                if (!used[i]) {
                    // choose one add to combination
                    combination.push(nums[i]);

                    // 标记已使用的值
                    used[i] = true;
                    // track next turn
                    backtrack(combination, len, nums, result, used);

                    // 回退已使用
                    used[i] = false;
                    // back track
                    combination.pop();
                }
            }
        }
    }

    public static List<List<Integer>> permute(int[] nums) {

        List<List<Integer>> res = new LinkedList<>();

        if (nums == null || nums.length == 0) {
            return res;
        }
        boolean[] used = new boolean[nums.length];

        backtrack(new Stack<>(), nums.length, nums, res, used);

        return res;
    }

    public static void main(String[] args) {
        int[] nums = {1, 2, 3};

        System.out.println("result: " + permute(nums));
    }
}
