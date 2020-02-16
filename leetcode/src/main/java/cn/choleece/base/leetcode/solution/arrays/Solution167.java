package cn.choleece.base.leetcode.solution.arrays;

import java.util.HashMap;
import java.util.Map;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-01-31 14:22
 **/
public class Solution167 {

    public static int[] twoSum(int[] numbers, int target) {
        int[] result = {-1, -1};

        if (numbers == null || numbers.length == 0) {
            return result;
        }

        Map<Integer, Integer> map = new HashMap<>(numbers.length);

        for (int i = 0; i < numbers.length; i++) {
            int gap = target - numbers[i];

            if (map.containsKey(gap)) {
                return new int[] {map.get(gap) + 1, i + 1};
            }

            int res = binarySearch(numbers, gap);
            if (res != -1 && res != i) {
                return new int[] {i + 1, res + 1};
            }

            map.put(numbers[i], i);
        }

        return result;
    }

    public static int binarySearch(int[] numbers, int target) {

        int left = 0;

        int right = numbers.length - 1;

        while (left <= right) {
            int mid = (left + right) / 2;

            if (numbers[mid] == target) {
                return mid;
            } else if (numbers[mid] < target) {
                left = mid + 1;
            } else {
                right = mid -1;
            }
        }

        return -1;
    }

    public static int[] twoSum1(int[] numbers, int target) {
        int[] result = {-1, -1};

        if (numbers == null || numbers.length == 0) {
            return result;
        }

        int left = 0;
        int right = numbers.length - 1;

        while (left < right) {
            int sum = numbers[left] + numbers[right];

            if (sum == target && left != right) {
                return new int[] {left + 1, right + 1};
            } else if (sum > target) {
                right--;
            } else {
                left++;
            }
        }

       return result;
    }

    public static void main(String[] args) {
        int[] numbers = {2, 7, 11, 15};
        int[] result = twoSum(numbers, 9);

        int[] result1 = twoSum1(numbers, 9);

        System.out.println("result: 0 idx is : " + result[0] + ", 1 idx is : " + result[1]);

        System.out.println("result1: 0 idx is : " + result1[0] + ", 1 idx is : " + result1[1]);
    }
}
