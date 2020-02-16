package cn.choleece.base.leetcode.solution.greedy;

import java.util.Arrays;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-02-05 21:21
 **/
public class Solution455 {

    public static int findContentChildren(int[] g, int[] s) {
        if (g == null || g.length == 0 || s == null || s.length == 0) {
            return 0;
        }

        Arrays.sort(g);
        Arrays.sort(s);

        int j = 0;
        int count = 0;
        for (int i = 0; i < g.length; i++) {
            while (j < s.length) {
                if (g[i] <= s[j]) {
                    count++;
                    j++;
                    break;
                }
                j++;
            }

            if (j == s.length) {
                break;
            }
        }
        return count;
    }

    public static int findContentChildren1(int[] g, int[] s) {
        if (g == null || g.length == 0 || s == null || s.length == 0) {
            return 0;
        }

        Arrays.sort(g);
        Arrays.sort(s);

        int i = 0;
        int j = 0;

        while (i < g.length && j < s.length) {
            if (g[i] <= s[j]) {
                i++;
            }
            j++;
        }

        return i;
    }

    public static void main(String[] args) {
        int[] g = {1, 2};
        int[] s = {1, 2, 3};

        System.out.println("result: " + findContentChildren(g, s));

        System.out.println("result: " + findContentChildren1(g, s));
    }

}
