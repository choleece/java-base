package cn.choleece.base.algorithm.sort;

/**
 * @author choleece
 * @Description: 希尔排序 选择一个特定的步长，将待排序数组进行分割，然后对分割后对数组进行直接插入排序,一趟下来，数组会慢慢变得相对有序，逐步缩小步长直至为1，接着走直接插入排序，这个时候数组会变得相对有序
 * 所以在做直接插入排序对时候，比较、移动的次数会减小，所以排序效率也会提高
 * 时间复杂度：平均：根据步长序列的不同而不同、最坏：O(nlog^2n)、最优：O(n)
 * 空间复杂度：O(1)
 * @Date 2019-09-22 22:38
 **/
public class ShellSort {

    public static void shellSort(int[] nums) {
        int step = nums.length;

        // 直到step为1
        while (true) {
            step /= 2;
            for (int i = 0; i < step; i++) {
                for (int j = i + step; j < nums.length; j += step) {
                    // 在这里进行插入排序, 从后向前比较插入
                    int temp = nums[j];
                    int k = j - step;
                    for (; k >= 0 ; k -= step) {
                        if (temp < nums[k]) {
                            nums[k + step] = nums[k];
                        } else {
                            break;
                        }
                    }
                    nums[k + step] = temp;
                }
            }

            if (step == 1) {
                break;
            }
        }
    }

    public static void main(String[] args) {
        int[] nums = {3, 4, 5, 7, 1, 2, 10};

        shellSort(nums);

        PrintArray.printArray(nums);
    }

}
