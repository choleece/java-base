package cn.choleece.base.jdk.sort;

/**
 * @description: 希尔排序 时间复杂度 O(nlogn)~O(n^2)
 * @author: choleece
 * @time: 2020-10-27 11:46
 */
public class ShellSort {

    /**
     * 希尔排序 在插入排序的基础上进行改进，也称为增量排序, 将数组分成间隔为gap的几个小数组，在小数组之间进行插入排序，然后知道gap为1，则为纯粹的插入排序
     * 我们在插入排序中知道，插入排序对于基本有序的序列，有O(n)的复杂度，因为没有从后往前的比较替换
     * @param arr 原始数组
     */
    private static void shellSort(int[] arr) {
        int gap = arr.length;
        while (true) {
            gap = gap / 2;
            // 每一轮分成gap个子序列进行插入排序，知道gap最后变成1， 因为插入排序有个判断，当后一个小于前一个，就不在往下进行
            for (int i = 0; i < gap; i++) {
                // 针对每一轮进行插入排序
                for (int j = i; j < arr.length - gap; j += gap) {
                    for (int k = j + gap; k > 0 ; k -= gap) {
                        // k - gap >= 0很关键，不然后续的k - gap有可能越界 需要进行交换
                        if (k - gap >= 0 && arr[k] < arr[k - gap]) {
                            int temp = arr[k];
                            arr[k] = arr[k - gap];
                            arr[k - gap] = temp;
                        } else {
                            break;
                        }
                    }
                }
            }

            // 当gap为1时，说明已经排序完成了
            if (gap == 1) {
                break;
            }
        }
    }

    public static void main(String[] args) {
        int[] arr = {5, 3, 7, 8, 2, 1, 9, 10, 0, 30, 45, 21, 22, 43, 39, 14};
        shellSort(arr);
        ArrayUtils.printArr(arr);
    }
}
