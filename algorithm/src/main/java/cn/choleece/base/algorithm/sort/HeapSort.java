package cn.choleece.base.algorithm.sort;

/**
 * @author choleece
 * @Description: 堆排序 堆是一个近似完全二叉树的结构（节点从左到右开始插入， 完成二叉树有个特性，左孩子的位置 = 父节点的位置 * 2 + 1， 右孩子的位置 = 父节点的位置 * 2 + 2， index从0开始算），
 * 并同时满足堆积的性质：即子结点的键值或索引总是小于（或者大于）它的父节点
 * https://www.cnblogs.com/Java3y/p/8639937.html
 * https://www.kancloud.cn/maliming/leetcode/742952
 * https://www.jianshu.com/p/0d383d294a80
 * @Date 2019-09-23 21:57
 **/
public class HeapSort {

    public static void heapSort(int[] nums) {
        int len = nums.length;
        buildMinHeap(nums, len);

        // 从最后一个节点开始，调整堆
        for (int i = len - 1; i > 0 ; i--) {
            swap(nums, 0, i);
            len--;
            heapify(nums, 0, len);
        }
    }

    /**
     * 构建最小堆，存在孩子节点最大的位置为(int) Math.floor(len / 2)
     * @param nums
     * @param len
     */
    public static void buildMinHeap(int[] nums, int len) {
        for (int i = (int) Math.floor(len / 2); i >= 0 ; i--) {
            heapify(nums, i, len);
        }
    }

    /**
     * 建堆，调整的过程
     * @param nums
     * @param i
     * @param len
     */
    public static void heapify(int[] nums, int i, int len) {
        // 父节点为i的左右孩子位置
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        int parentIdx = i;

        //  与左孩子进行比较，如果左孩子比父节点大，如果左孩子比父节点大，则进行待交换
        if (left < len && nums[left] > nums[parentIdx]) {
            parentIdx = left;
        }

        // 将右孩子与父节点（有可能是上一步的左孩子，因为左孩子比父节点大，所以跟左孩子进行比较），如果右孩子较大，那么进行交换
        if (right < len && nums[right] > nums[parentIdx]) {
            parentIdx = right;
        }

        // 看看是否父节点比左右孩子都小，如果不是，那么需要进行交换，交换完了后，以被交换的节点当作父节点，重新调整堆
        if (parentIdx != i) {
            swap(nums, i, parentIdx);
            heapify(nums, parentIdx, len);
        }
    }

    /**
     * 交换节点
     * @param nums
     * @param i
     * @param j
     */
    public static void swap(int[] nums, int i, int j) {
        int tmp = nums[j];
        nums[j] = nums[i];
        nums[i] = tmp;
    }

    public static void main(String[] args) {
        int[] nums = {4, 5, 8, 1, 2, 0, 10};

        heapSort(nums);

        PrintArray.printArray(nums);
    }
}
