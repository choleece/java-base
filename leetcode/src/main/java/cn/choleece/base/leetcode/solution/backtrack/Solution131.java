package cn.choleece.base.leetcode.solution.backtrack;

import java.util.*;

/**
 * @description: TODO
 * @author: choleece
 * @time: 2020-06-13 08:38
 */
public class Solution131 {

    public static List<List<String>> partition(String s) {

        List<List<String>> result = new LinkedList();

        backtrack(new Stack<String>(), s, result);

        return result;
    }

    public static void backtrack(Stack<String> subStrList, String str, List<List<String>> result) {

        if ((str == null || str.length() == 0) && subStrList.size() > 0) {

            // 这里需要深克隆
            result.add(new ArrayList<>(subStrList));

            // 一定要有termination
            return;
        }

        for(int i = 1; i <= str.length(); i++) {
            String subStr = str.substring(0, i);
            if (isPalindromic(subStr)) {

                // 选择一个
                subStrList.push(subStr);
                // 选择的，待选择的， 结果
                backtrack(subStrList, str.substring(i), result);
                // 选择回退
                subStrList.pop();
            }
        }
    }

    public static boolean isPalindromic(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }

        if (str.length() == 1) {
            return true;
        }

        char[] chars = str.toCharArray();

        int j = chars.length - 1;
        for(int i = 0; i <= j; i++) {
            if (chars[i] != chars[j]) {
                return false;
            }
            j--;
        }

        return true;
    }

    public static void main(String[] args) {
        String str = "cbbbcc";

        System.out.println(partition(str));
    }

}