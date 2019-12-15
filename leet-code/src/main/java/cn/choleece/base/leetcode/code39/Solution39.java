package cn.choleece.base.leetcode.code39;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-11-13 21:26
 **/
public class Solution39 {

    Map<Integer, Integer> map = new HashMap<>();

    public static List<List<Integer>> combinationSum(int[] candidates, int target) {
        for (int i = 0; i < candidates.length; i++) {
            int val = candidates[i];


        }

        return null;
    }

    public static void main(String[] args) {
        int target = 7;
        int[] candidates = {2, 3, 6, 7};

        System.out.println(combinationSum(candidates, target));
    }
}
