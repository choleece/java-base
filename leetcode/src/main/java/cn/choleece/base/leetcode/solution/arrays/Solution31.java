package cn.choleece.base.leetcode.solution.arrays;

/**
 * @author choleece
 * @Description:
 * @Date 2020-04-18 15:45
 **/
public class Solution31 {

    public static void main(String[] args) {
        int[] nums = {3, 2, 1};

        nextPermutation(nums);

        for (int i = 0; i < nums.length; i++) {
            System.out.print(nums[i] + " ");
        }
        System.out.println();
    }

    /**
     * 找规律题，先从后往前找到开始降序的第一个，然后对它后边的进行反转，最后如果降序的位置存在，则寻找第一比该位置小的数字，并进行交换
     * @param nums
     */
    public static void nextPermutation(int[] nums) {
        int j = nums.length - 1;
        int i = nums.length - 1;
        while (j > 0 && nums[j] <= nums[j - 1]) {
            j--;
        }

        reverse(nums, j, nums.length - 1);

        if (j > 0) {
            for (int k = j; k < nums.length; k++) {
                if (nums[j - 1] < nums[k]) {
                    swap(nums, j - 1, k);
                    break;
                }
            }
        }
    }

    /**
     * 交换两个数字
     * @param nums
     * @param i
     * @param j
     */
    public static void swap(int[] nums, int i, int j) {
        int tmp = nums[i];

        nums[i] = nums[j];
        nums[j] = tmp;
    }

    /**
     * 反转数组
     * @param nums
     * @param i
     * @param j
     */
    public static void reverse(int[] nums, int i, int j) {
        while (i < j) {
            swap(nums, i, j);
            i++;
            j--;
        }
    }
}
