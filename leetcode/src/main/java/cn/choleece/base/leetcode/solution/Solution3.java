package cn.choleece.base.leetcode.solution;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-01-28 23:11
 **/
public class Solution3 {

    public static int lengthOfLongestSubstring(String s) {
        if (s == null || s.length() < 1) {
            return 0;
        }

        char[] chars = s.toCharArray();

        HashMap<Character, Integer> map = new HashMap<>();
        // pre为最近出现重复字符的位置
        int pre = -1;
        int max = 0;
        for (int i = 0; i < chars.length; i++) {
           Integer idx = map.get(chars[i]);
           if (idx != null) {
               pre = Math.max(pre, idx);
           }

           max = Math.max(max, i - pre);
           map.put(chars[i], i);
        }

        return max;
    }

    public static void main(String[] args) {
        String s = "pwwkew";
        System.out.println(lengthOfLongestSubstring(s));
    }
}
