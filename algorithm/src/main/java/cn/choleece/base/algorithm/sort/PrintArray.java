package cn.choleece.base.algorithm.sort;

/**
 * 数组打印
 * @author choleece
 */
public class PrintArray {

    public static void printArray(int[] nums) {
        for (int i = 0; i < nums.length ; i++) {
            System.out.printf(nums[i] + " ");
        }
        System.out.println();
    }

}
