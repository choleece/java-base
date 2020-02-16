package cn.choleece.base.leetcode.solution.divideconquer;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-01-31 20:32
 **/
public class Solution215 {

    public static int findKthLargest(int[] nums, int k) {

        Arrays.sort(nums);

        return nums[nums.length - k];
    }

    public static int findKthLargest1(int[] nums, int k) {
        Queue<Integer> priorityQueue = new PriorityQueue<>();

        for (int i = 0; i < nums.length; i++) {
            if (i < k) {
                priorityQueue.add(nums[i]);
            } else if (nums[i] > priorityQueue.peek()) {
                priorityQueue.poll();
                priorityQueue.add(nums[i]);
            }
        }

        return priorityQueue.peek();
    }

    public static void main(String[] args) {
        int[] nums = {4, 3, 5, 1, 6};

        System.out.println("result: the k largest element is " + findKthLargest(nums, 1));

        System.out.println("result: the k largest element is " + findKthLargest1(nums, 1));
    }
}
