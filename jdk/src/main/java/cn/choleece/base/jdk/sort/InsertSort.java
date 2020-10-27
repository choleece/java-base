package cn.choleece.base.jdk.sort;

/**
 * @description: 插入排序  时间复杂度O(n^2)
 * @author: choleece
 * @time: 2020-10-27 11:31
 */
public class InsertSort {

    /**
     * 插入排序的思路, 默认之前的数据是排好序的，然后依次往前比较，如果比之间小，则插入
     * @param arr
     */
    private static void insertSort(int[] arr) {
        // 只需要比较arr.length - 1轮
        for (int i = 0; i < arr.length - 1; i++) {
            // 这里采用从尾部开始比较，是为了插入后不频繁移动后面的数据，直接从尾部比较，只需要做到交换即可
            for (int j = i + 1; j > 0; j--) {
                // 需要交换
                if (arr[j] < arr[j - 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j - 1];
                    arr[j - 1] = temp;
                } else {
                    // 当遇到第一个不需要交换的时候，前面的数据就不需要比较了, 这里很关键，阔以减少比较次数
                    break;
                }
            }
        }
    }

    private static void insertSort1(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            // 这里稍微简化一下代码
            for (int j = i + 1; j > 0 && arr[j] < arr[j - 1]; j--) {
                int temp = arr[j];
                arr[j] = arr[j - 1];
                arr[j - 1] = temp;
            }
        }
    }

    public static void main(String[] args) {
        int[] arr = {4, 5, 3, 2, 6, 8, 1};
        insertSort(arr);
        ArrayUtils.printArr(arr);

        int[] arr1 = {10, 5, 3, 2, 6, 8, 1};
        insertSort1(arr1);
        ArrayUtils.printArr(arr1);
    }
}
