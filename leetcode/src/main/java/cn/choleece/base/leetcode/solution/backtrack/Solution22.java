package cn.choleece.base.leetcode.solution.backtrack;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * @description: 这里也可以参考 https://blog.csdn.net/qq_17550379/article/details/84784005
 * @author: choleece
 * @time: 2020-02-15 21:54
 */
public class Solution22 {


    static String LEFT_PARENTHESIS = "(";

    static String RIGHT_PARENTHESIS = ")";

    static List<String> CHOICE_LIST = Arrays.asList(LEFT_PARENTHESIS, RIGHT_PARENTHESIS);

    public static void backtrack(String combination, int n, List<String> result) {
        if (n == 0) {
            if (matchParenthesis(combination)) {
                result.add(combination);
            }
        } else {
            for (int i = 0; i < CHOICE_LIST.size(); i++) {
                String next = CHOICE_LIST.get(i);
                backtrack(combination + next,n - 1, result);
            }
        }
    }

    public static boolean matchParenthesis(String parenthesis) {
        Stack<String> stack = new Stack<>();

        for (int i = 0; i < parenthesis.length(); i++) {
            String cur = parenthesis.substring(i, i + 1);

            if (!stack.isEmpty()) {
                String top = stack.peek();
                if ((LEFT_PARENTHESIS.equals(top) && RIGHT_PARENTHESIS.equals(cur))) {
                    stack.pop();
                } else {
                    stack.push(cur);
                }
            } else {
                stack.push(cur);
            }
        }

        return stack.isEmpty();
    }

    public static List<String> generateParenthesis(int n) {
        List<String> result = new LinkedList<>();

        if (n <= 0) {
            return result;
        }

        backtrack("", 2 * n, result);

        return result;
    }

    public static List<String> generateParenthesis20200524(int n) {
        List<String> result = new LinkedList<>();

        if (n <= 0) {
            return result;
        }

        // backtrack
        backtrack20200524("", 2 * n, result);

        return result;
    }

    public static void backtrack20200524(String combination, int nextN, List<String> result) {
        if (nextN == 0) {
            if (matchParenthesis20200524(combination)) {
                result.add(combination);
            }
            return;
        }

        // loop choose
        for (String str : CHOICE_LIST) {
            // 这里有一个优化点，如果发现左括号已经超过了n,那么可以结束后面点递归
            backtrack20200524(combination + str, nextN - 1, result);
        }
    }

    public static boolean matchParenthesis20200524(String parenthesis) {
        if (parenthesis == null || parenthesis.length() == 0) {
            return false;
        }

        Stack<String> stack = new Stack();
        for (Character c : parenthesis.toCharArray()) {
            String str = String.valueOf(c);
            if (!stack.isEmpty()) {
                String top = stack.peek();
                if (LEFT_PARENTHESIS.equals(top) && RIGHT_PARENTHESIS.equals(str)) {
                    stack.pop();
                } else {
                    stack.push(str);
                }
            } else {
                stack.push(str);
            }
        }

        return stack.isEmpty();
    }

    public static void main(String[] args) {
        int n = 2;

        System.out.println("result: " + generateParenthesis(n));
        System.out.println("result: " + generateParenthesis20200524(n));
    }
}
