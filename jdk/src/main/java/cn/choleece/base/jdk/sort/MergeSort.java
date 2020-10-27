package cn.choleece.base.jdk.sort;

/**
 * @description: 归并排序 时间复杂度 O(N*logN)
 * @author: choleece
 * @time: 2020-10-27 15:55
 */
public class MergeSort {

    /**
     * 归并排序的思路就是将两个有序的数组进行合并
     * 然后通过分治，将数组切分成不通大小的数组，最后是切分成大小为1的数组，然后两两合并，然后就成了最终的一个排序好的数组
     * @param arr
     * @return
     */
    private static int[] mergeSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return arr;
        }

        int midLength = arr.length / 2;
        int[] leftDest = new int[midLength];
        System.arraycopy(arr, 0, leftDest, 0, midLength);

        int[] rightDest = new int[arr.length - midLength];
        System.arraycopy(arr, midLength, rightDest, 0, arr.length - midLength);

        return merge(mergeSort(leftDest), mergeSort(rightDest));
    }

    private static int[] merge(int[] arrA, int[] arrB) {
        if (arrA == null || arrA.length == 0) {
            return arrB;
        }
        if (arrB == null || arrB.length == 0) {
            return arrA;
        }

        int[] result = new int[arrA.length + arrB.length];
        int i = 0, j = 0;
        int k = 0;
        while (i < arrA.length && j < arrB.length) {
            result[k++] = arrA[i] < arrB[j] ? arrA[i++] : arrB[j++];
        }

        while (i < arrA.length) {
            result[k++] = arrA[i++];
        }
        while (j < arrB.length) {
            result[k++] = arrB[j++];
        }

        return result;
    }

    public static void main(String[] args) {
        int[] arr = {6, 4, 5, 2, 3};
        ArrayUtils.printArr(mergeSort(arr));
    }
}
