package cn.choleece.base.leetcode.solution.arrays;

import java.util.Arrays;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-03-08 20:58
 **/
public class Solution88 {

    public static void merge(int[] nums1, int m, int[] nums2, int n) {
        for (int i = 0; i < n; i++) {
            nums1[m + i] = nums2[i];
        }

        Arrays.sort(nums1);
    }

    public static void insertSort(int[] nums1, int m, int[] nums2, int n) {
        for (int i = 0; i < n; i++) {
            int nums1Len = m;
            int a = nums2[i];
            while (nums1Len > 0) {
                if (a > nums1[nums1Len - 1]) {
                    nums1[nums1Len] = a;
                    break;
                } else {
                    nums1[nums1Len] = nums1[--nums1Len];
                }
            }
            if (nums1Len == 0) {
                nums1[nums1Len] = a;
            }
            m++;
        }
    }

    /**
     * 这里因为是已排好序的，是否可以考虑使用二分
     * @param args
     */
    public static void main(String[] args) {
        int[] nums1 = {0};
        int[] nums2 = {1};

        merge(nums1, 3, nums2, 3);
        insertSort(nums1, 0, nums2, 1);
        for (int i = 0; i < nums1.length; i++) {
            System.out.print(" " + nums1[i]);
        }
        System.out.println();
    }
}
