package cn.choleece.base.leetcode.solution.greedy;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-02-03 22:20
 **/
public class Solution392 {

    public static boolean isSubsequence(String s, String t) {

        if (t == null || s == null || t.length() < s.length()) {
            return false;
        }

        char[] charsOfS = s.toCharArray();

        int firstIdx = 0;
        for (char c : charsOfS) {
            firstIdx = t.indexOf(c) + 1;
            if (firstIdx == 0) {
                return false;
            }

            t = t.substring(firstIdx);
        }

        return true;
    }

    public static boolean isSubSequence1(String s, String t) {
        if (t == null || s == null || t.length() < s.length()) {
            return false;
        }

        return isSubSequenceWithRecursion(s, 0, t);
    }

    public static boolean isSubSequenceWithRecursion(String s, int idxOfS, String t) {
        if (s.length() == 0) {
            return true;
        }

        int firstIdx = t.indexOf(s.charAt(idxOfS)) + 1;
        if (firstIdx == 0) {
            return false;
        }

        if (idxOfS == s.length() - 1) {
            return true;
        }

        return isSubSequenceWithRecursion(s, idxOfS + 1, t.substring(firstIdx));
    }

    public static boolean isSubSeqWithRecursion(String s, String t) {
        if (s.length() == 0) {
            return true;
        }

        int idx = t.indexOf(s.charAt(0)) + 1;
        if (idx == 0) {
            return false;
        }
        return isSubSeqWithRecursion(s.substring(1), t.substring(idx));
    }

    public static void main(String[] args) {
        String s = "";
        String t = "abcde";

        System.out.println("if the s is sub sequence of t, the result is : " + isSubsequence(s, t));

        System.out.println("if the s is sub sequence of t with recursion, the result is : " + isSubSequence1(s, t));

        System.out.println("if the s is sub sequence of t with recursion, the result is : " + isSubSeqWithRecursion(s, t));
    }

}
