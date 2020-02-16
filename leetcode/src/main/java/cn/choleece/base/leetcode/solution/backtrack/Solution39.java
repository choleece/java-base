package cn.choleece.base.leetcode.solution.backtrack;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * @description: TODO
 * @author: choleece
 * @time: 2020-02-16 13:19
 */
public class Solution39 {

    public static void backtrack(Stack<Integer> combination, int residue, int start, int len, int[] candidates,  List<List<Integer>> result) {

        // if track is ok
        if (residue == 0) {
            // 这里是关键，combination为引用，这里需要将值拷贝出来
            result.add(new LinkedList<>(combination));
        } else {
            for (int i = start; i < len; i++) {
                if (residue - candidates[i] >= 0) {
                    // choose one add to stack
                    combination.push(candidates[i]);
                    // 这里从i开始，而不是i + 1, 是因为可以重复利用自身
                    backtrack(combination, residue - candidates[i], i, len, candidates, result);
                    // un choose 从track 中撤销上面的选择
                    combination.pop();
                }
            }
        }
    }

    public static List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> result = new LinkedList<>();

        if (candidates == null || candidates.length == 0) {
            return result;
        }

        Stack<Integer> combination = new Stack<>();

        backtrack(combination, target, 0, candidates.length, candidates, result);

        return result;
    }

    public static void main(String[] args) {
        int[] candidates = {8, 7, 4, 3};
        int target = 11;

        System.out.println("result: " + combinationSum(candidates, target));
    }

}
