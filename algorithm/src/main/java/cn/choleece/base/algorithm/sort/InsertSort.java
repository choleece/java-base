package cn.choleece.base.algorithm.sort;

/**
 * 插入排序 https://zh.wikipedia.org/wiki/%E6%8F%92%E5%85%A5%E6%8E%92%E5%BA%8F
 * 时间负责度O(n^2) 空间负责度O(1) 优化点，在查询的时候，可以用二分查找
 * @author choleece
 */
public class InsertSort {

    public static void insertionSort1(int[] nums) {

        if (nums == null || nums.length < 2) {
            return;
        }

        for (int i = 0; i < nums.length; i++) {
            int val = nums[i];
            for (int j = 0; j < i; j++) {
                if (val < nums[j]) {
                    // 向后移动数据
                    for (int k = i; k > j;) {
                        nums[k] = nums[--k];
                    }
                    nums[j] = val;
                    break;
                }
            }
        }
    }

    public static void insertionSort(int[] nums) {

        if (nums == null || nums.length < 2) {
            return;
        }

        for (int i = 0; i < nums.length; i++) {
            int val = nums[i];
            int j = i - 1;
            // 从后往前比较，如果比前一个小，则交换
            while (j >= 0 && val < nums[j]) {
                nums[j + 1] = nums[j];
                j--;
            }
            // 移动数组的空位或者不移动就是自身
            nums[j + 1] = val;
        }
    }

    public static void main(String[] args) {

        int[] nums = {1, 4, 5, 6, 2, 1, 3};

        PrintArray.printArray(nums);

        insertionSort(nums);

        PrintArray.printArray(nums);
    }

}
