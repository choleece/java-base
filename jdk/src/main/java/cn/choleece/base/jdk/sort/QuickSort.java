package cn.choleece.base.jdk.sort;

/**
 * @description: 快速排序 时间复杂度 O(N*logN)
 * @author: choleece
 * @time: 2020-10-27 14:25
 */
public class QuickSort {

    private static void quickSort(int[] arr) {
        quickSort(arr, 0, arr.length - 1);
    }

    private static void quickSort(int[] arr, int left, int right) {
        // recursion termination
        if (arr == null || arr.length < 2 || left >= right) {
            return;
        }

        // 选取中间位置的一个数据，从两头进行比较
        int midVal = arr[(left + right) / 2], i = left, j = right;
        // 当推出while循环后，说明数据已经被midVal分成了两部分，左边当部分都小于midVal, 右边都部分都大于midVal
        while (i <= j) {

            // 从左边开始
            while (arr[i] < midVal) {
                i++;
            }
            // 从右边开始
            while (arr[j] > midVal) {
                j--;
            }
            // 到此位置说明就是左边到比选中到那个数据大，右边到比选中到位置小，需要将两个数据进行交换
            if (i < j) {
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                // 左边进1
                i++;
                // 右边减1
                j--;
            } else {
                i++;
            }
        }

        // 采用分治策略，左边进行同样的操作, 针对左右进行递归
        quickSort(arr, left, i - 1);
        quickSort(arr, i + 1, right);
    }

    public static void main(String[] args) {
        int[] arr = {5, 3, 2, 6, 8, 9, 10};
        quickSort(arr);
        ArrayUtils.printArr(arr);
    }

}
