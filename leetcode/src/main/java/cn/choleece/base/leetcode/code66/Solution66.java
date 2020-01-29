package cn.choleece.base.leetcode.code66;

import java.util.Stack;

/**
 * Given a non-empty array of digits representing a non-negative integer, plus one to the integer.
 *
 * The digits are stored such that the most significant digit is at the head of the list, and each element in the array contain a single digit.
 *
 * You may assume the integer does not contain any leading zero, except the number 0 itself.
 */
public class Solution66 {

    public static int[] plusOne1(int[] digits) {
        if (digits == null || digits.length == 0) {
            return null;
        }
        return plusOne(digits, digits.length - 1);
    }

    /**
     * recursion
     * @param digits
     * @param index
     * @return
     */
    public static int[] plusOne(int[] digits, int index) {
        if (digits == null || digits.length == 0) {
            return null;
        }
        int lastBit = digits[index];
        if (lastBit < 9) {
            digits[index] = lastBit + 1;
            return digits;
        } else {
            digits[index] = 0;
            if (index == 0) {
                int[] arr = new int[digits.length + 1];
                arr[0] = 1;
                for (int i = 0; i < digits.length; i++) {
                    arr[i + 1] = digits[i];
                }
                return arr;
            }
            return plusOne(digits, index - 1);
        }
    }

    /**
     * no recursion
     * @param digits
     * @return
     */
    public static int[] plusOne(int[] digits) {
        if (digits == null || digits.length == 0) {
            return null;
        }

        int[] arr = new int[digits.length + 1];
        arr[0] = 1;
        for (int i = digits.length - 1; i >= 0; i--) {
            int lastBit = digits[i];
            if (lastBit < 9) {
                digits[i] = lastBit + 1;
                return digits;
            } else {
                digits[i] = 0;
            }
            arr[i + 1] = 0;
        }
        return arr;
    }

    public static void main(String[] args) {
        int[] digits = {8, 9};

        digits = plusOne(digits);

        for (int i = 0; i < digits.length; i++) {
            System.out.println(digits[i]);
        }
        System.out.println();
    }
}
