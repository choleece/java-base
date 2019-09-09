package cn.choleece.base.algorithm.sort;

import java.util.Arrays;
import java.util.List;

public class BubbleSort {

    public static void bubbleSort(List<Integer> nums) {
        for (int i = 0; i < nums.size(); i++) {
            for (int j = i + 1; j < nums.size(); j++) {
                if (nums.get(j) < nums.get(i)) {
                    int tmp = nums.get(j);
                    nums.set(j, nums.get(i));
                    nums.set(i, tmp);
                }
            }
        }
    }

    public static void main(String[] args) {
        List<Integer> nums = Arrays.asList(1, 4, 2, 4, 5, 6, 8, 7);

        System.out.println(nums);

        bubbleSort(nums);

        System.out.println(nums);
    }

}
