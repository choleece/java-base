package cn.choleece.base.algorithm.sort;

/**
 * 快速排序 https://zh.wikipedia.org/wiki/%E5%BF%AB%E9%80%9F%E6%8E%92%E5%BA%8F
 * 时间复杂度O(n log n), 最坏对情况为已经排好序对，时间复杂度为O(n^2)
 * 空间负责度为O(1)
 * @author choleece
 */
public class QuickSort {

    public static void quickSort(int[] nums) {
        quickSortRecursion(nums, 0, nums.length - 1);
    }

    public static void quickSortRecursion(int[] nums, int left, int right) {

        // recursion terminal condition
        if (nums == null || nums.length < 2 || left >= right) {
            return;
        }

        int midVal = nums[(left + right) / 2], i = left, j = right;
        while (i <= j) {

            // 如果比中间值小，则指针右移动
            while (nums[i] < midVal) {
                i++;
            }

            // 如果比中间值大，则指针左移
            while (nums[j] > midVal) {
                j--;
            }

            if (i < j) {
                // 到此处说明左边值比midVal大，右边值比midVal小，则需要对两边对数据进行交换
                int tmp = nums[j];
                nums[j] = nums[i];
                nums[i] = tmp;
                i++;
                j--;
            } else if (i == j) {
                // 当两者相遇 i右移一位
                i++;
            }
        }

        // 递归左右两边对值
        quickSortRecursion(nums, left, j);
        quickSortRecursion(nums, i, right);
    }

    public static void main(String[] args) {
        int[] nums = {1, 4, 5, 6, 2, 1, 3};

        PrintArray.printArray(nums);

        quickSort(nums);

        PrintArray.printArray(nums);
    }

}
