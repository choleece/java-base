package cn.choleece.base.algorithm.search;

/**
 * @author choleece
 * @Description: 二分查找, 二分查找是建立在已经排好序的基础上进行的查找算法，且二分查找是建立在顺序表的基础上进行的（数组），比如链表就不可以满足情况，
 * 顺序表有个苛刻的条件就是内存必须是连续的，假设现在有个1GB的数组，内存有2G但是不连续，也是不能用二分查找进行的
 * 时间负责度 O(nlgn)
 * @Date 2019-09-28 15:39
 **/
public class BinarySearch {

    public static int binarySearch(int[] nums, int val) {
        int left = 0, right = nums.length - 1;

        while (left <= right) {
            int mid = (left + right) / 2;
            int tmp = nums[mid];
            if (val == tmp) {
                return mid;
            } else if (tmp > val) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return -1;
    }

    public static int binarySearchWithRecursion(int[] nums, int val) {
        return binaryWithRecursion(nums, 0, nums.length - 1, val);
    }

    public static int binaryWithRecursion(int[] nums, int left, int right, int val) {
        if (left > right) {
            return -1;
        }

        int mid = (left + right) / 2;

        if (nums[mid] == val) {
            return mid;
        } else if (nums[mid] > val) {
            return binaryWithRecursion(nums, left, mid - 1, val);
        } else {
            return binaryWithRecursion(nums, mid + 1, right, val);
        }
    }

    public static void main(String[] args) {
        int[] nums = {1, 2, 3, 4, 5, 6, 7, 8};

        System.out.println(binarySearch(nums, 1));

        System.out.println(binarySearchWithRecursion(nums, 8));
    }

}
