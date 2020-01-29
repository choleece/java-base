package cn.choleece.base.leetcode.code17;

import java.util.*;

/**
 * @author choleece
 * @Description: Given a string containing digits from 2-9 inclusive, return all possible letter combinations that the number could represent.
 *
 * A mapping of digit to letters (just like on the telephone buttons) is given below. Note that 1 does not map to any letters
 * @Date 2019-09-19 22:23
 **/
public class Solution17 {

    public static final Map<String, Character[]> map = new HashMap<>(8);

    static {
        map.put("2", new Character[]{'a', 'b', 'c'});
        map.put("3", new Character[]{'d', 'e', 'f'});
        map.put("4", new Character[]{'g', 'h', 'i'});
        map.put("5", new Character[]{'j', 'k', 'l'});
        map.put("6", new Character[]{'m', 'n', 'o'});
        map.put("7", new Character[]{'p', 'q', 'r', 's'});
        map.put("8", new Character[]{'t', 'u', 'v'});
        map.put("9", new Character[]{'w', 'x', 'y', 'z'});
    }

    public List<String> letterCombinations(String digits) {
        List<String> list = new ArrayList<>();

        if (digits == null || digits.equals("") || digits.equals("1")) {
            return list;
        }

        char[] digs = digits.toCharArray();

        Stack<Character[]> stack = new Stack<>();
        stack.push(map.get(digs[0]));

        Set<Character[]> visitedSet = new HashSet();
        visitedSet.add(map.get(digs[0]));

        for (int i = 0; i < digs.length; i++) {

        }

        return null;
    }

    public static void main(String[] args) {

    }
}
