package cn.choleece.base.leetcode.solution.string;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-01-29 20:09
 **/
public class Solution5 {

    public static String longestPalindrome(String s) {
        if (s == null || s.length() < 1) {
            return s;
        }

        int len = s.length();
        Boolean[][] result = new Boolean[len][len];
        int firstIdx = 0, max = 0;

        for (int i = 0; i < len; i++) {
            // 代表单个字母满足回文情况
           result[i][i] = true;
           // 两个字母也满足回文情况
           if (i < len - 1 && s.charAt(i) == s.charAt(i + 1)) {
               result[i][i + 1] = true;
               firstIdx = i;
               max = 2;
           }
        }

        // m代表回文字符串的长度 如果长度 > 3,则理论上存在3 -- len的长度（从小往大计算）
        for (int m = 3; m <= len; m++) {
            for (int i = 0; i <= len - m; i++) {
                // 子字符串结束的位置
                int j = i + m - 1;
                // 满足i == j,并且这两个坐标之间的字符串满足回文(构造状态转移公式)
                if (s.charAt(i) == s.charAt(j) && (result[i + 1][j - 1] != null)) {
                    result[i][j] = true;
                    firstIdx = i;
                    max = m;
                }
            }
        }

        if (firstIdx == 0 && max == 0) {
            return String.valueOf(s.charAt(0));
        }

        return s.substring(firstIdx, firstIdx + max);
    }

    public static String longestPalindrome1(String s) {
        if (s == null || s.length() < 1) {
            return s;
        }

        int len = s.length();
        int firstIdx = 0, max = 0;

        for(int i = 0; i < len; ) {
            int left = i, right = i;
            char c = s.charAt(i);

            // 判断是否存在重复的字符，如果重复，则一直向右
            while (right + 1 < len && s.charAt(right + 1) == c) {
                right++;
            }

            i = 1 + (left != right ? right : i);

            // 找到中间位置的字符串，然后左右两个指针进行比对
            while (left >= 0 && right < len && s.charAt(left) == s.charAt(right)) {
                left--;
                right++;
            }

            if (max < right - left - 1) {
                max = right - left - 1;
                firstIdx = left + 1;
            }
        }

        return s.substring(firstIdx, firstIdx + max);
    }

    public static void main(String[] args) {
        String s = "cccddcc";
        System.out.println("result: " + longestPalindrome(s));
        System.out.println("result: " + longestPalindrome1(s));
    }
}
