package cn.choleece.base.jdk.sort;

/**
 * @description: 冒泡排序 时间复杂度O(n^2)
 * @author: choleece
 * @time: 2020-10-27 10:20
 */
public class BubbleSort {

    /**
     *
     * 每一轮前后交换，这样每一轮前后交换，结束后就阔以将最大或者最小放到底层
     * @param arr 原始数组
     */
    private static void bubbleSort(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            // 这里需要从尾部往前递进
            for (int j = arr.length - 1; j > i; j--) {
                if (arr[j - 1] > arr[j]) {
                    int temp = arr[j - 1];
                    arr[j - 1] = arr[j];
                    arr[j] = temp;
                }
            }
        }
    }

    /**
     * 每一轮前后交换， 从后往前比较， 当进行一轮遍历的时候，发现没有交换过，说明顺序已经排好，不需要再进行下一阶段的比较
     * @param arr 原始数组
     */
    private static void bubbleSort1(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            boolean flag = false;
            // 这里需要从尾部往前递进
            for (int j = arr.length - 1; j > i; j--) {
                if (arr[j - 1] > arr[j]) {
                    int temp = arr[j - 1];
                    arr[j - 1] = arr[j];
                    arr[j] = temp;
                    flag = true;
                }
            }
            if (!flag) {
                System.out.println("没有交换，说明已经排好序");
                break;
            }
        }
    }

    public static void main(String[] args) {
        int[] arr = {2, 4, 5, 6, 3, 8, 10};
        bubbleSort(arr);
        ArrayUtils.printArr(arr);

        int[] arr1 = {2, 3, 4, 5, 6, 7, 8, 10, 9};
        bubbleSort1(arr1);
        ArrayUtils.printArr(arr1);
    }

}
