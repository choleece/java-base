package cn.choleece.base.algorithm.sort;

/**
 * @author choleece
 * 选择排序
 * 1、首先在未排序序列中找到最小（大）元素，若最后所得最小（大）值的位置和当前对比的元素位置不同，就交换存放到排序序列的当前对比的元素位置，若相同直接跳过从下一个开始。
 * 2、再从剩余未排序元素中继续寻找最小（大）元素的位置。
 * 3、以此类推，重复以上步骤，直到所有元素均排序完毕总是在未排序的数组里选择最小（或最大）
 * 时间复杂度：平均：O(n^2)、最坏：O(n^2)、最优：O(n^2)
 * @Date 2019-09-23 21:26
 **/
public class SelectionSort {

    public static void selectionSort(int[] nums) {

        int min = 0;

        // 只记录最小的位置，不必像冒泡排序一样每次进行比较交换
        for (int i = 0; i < nums.length; i++) {
            min = i;
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[j] < nums[min]) {
                    min = j;
                }
            }

            if (min > i) {
                int tmp = nums[min];
                nums[min] = nums[i];
                nums[i] = tmp;
            }
        }
    }

    public static void main(String[] args) {

        int[] nums = {5, 2, 3, 4, 1, 6, 9, 8};

        selectionSort(nums);

        PrintArray.printArray(nums);
    }

}
