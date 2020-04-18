package cn.choleece.base.leetcode.solution.arrays;

import java.util.*;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-04-18 16:42
 **/
public class Solution39 {

    public static void main(String[] args) {
        int[] combinations = {1, 2, 4};
        int target = 7;

        List<List<Integer>> result = combinationSum(combinations, target);

        System.out.println(result);
    }

    public static List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> result = new LinkedList<>();
        backtrack(new LinkedList<>(), 0, target, candidates, result, 0);
        return result;
    }

    public static void backtrack(List<Integer> track, int sum, int target, int[] candidates, List<List<Integer>> result, int cur) {
        if (sum > target) {
            return;
        }
        if (sum == target) {
            result.add(new LinkedList<>(track));
            return;
        }


        for (int i = cur; i < candidates.length; i++) {
            track.add(candidates[i]);
            backtrack(track, sum + candidates[i], target, candidates, result, i);
            track.remove(track.size() - 1);
        }
    }
}
