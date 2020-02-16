package cn.choleece.base.leetcode.solution.backtrack;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * @description: TODO
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

    public static void main(String[] args) {
        int n = 1;

        System.out.println("result: " + generateParenthesis(n));
    }

}
