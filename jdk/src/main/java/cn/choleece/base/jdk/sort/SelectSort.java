package cn.choleece.base.jdk.sort;

/**
 * @description: 选择排序  时间复杂度O(n^2)
 * @author: choleece
 * @time: 2020-10-27 10:52
 */
public class SelectSort {

    /**
     * 选择排序的思路就是在每一轮遍历过程中，选择当前轮最小的数
     * @param arr 原始数组
     */
    private static void selectSort(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            int min = arr[i];
            // 每一轮选出来一个最小的
            for (int j = i + 1; j < arr.length; j++) {
                if (min > arr[j]) {
                    arr[i] = arr[j];
                    arr[j] = min;
                    min = arr[j];
                }
            }
        }
    }

    private static void selectSort1(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            int minIdx = i;
            // 每一轮选出来一个最小的
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[minIdx] > arr[j]) {
                    minIdx = j;
                }
            }

            // 当最小值不是原始位置，则需要进行交换
            if (minIdx != i) {
                int tem = arr[i];
                arr[i] = arr[minIdx];
                arr[minIdx] = tem;
            }
        }
    }

    public static void main(String[] args) {
        int[] arr = {4, 5, 6, 2, 1, 8, 9};
        selectSort(arr);
        ArrayUtils.printArr(arr);

        int[] arr1 = {9, 8, 4, 10, 12, 33, 1, 2};
        selectSort1(arr1);
        ArrayUtils.printArr(arr1);
    }

}
