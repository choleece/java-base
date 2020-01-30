package cn.choleece.base.leetcode.solution;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-01-30 23:20
 **/
public class solution69 {

    public static int mySqrt(int x) {

        if (x <= 0) {
            return 0;
        }

        return helper(x / 2, x);
    }

    public static int helper(int n, int x) {

        Long s = Long.valueOf(n) * Long.valueOf(n);
        Long s1 = Long.valueOf(n + 1) * Long.valueOf(n + 1);

        if (s <= x && s1 > x) {
            return n;
        }

        if (s > x) {
            return helper(n / 2, x);
        }

        return helper(n + 1, x);
    }

    public static int mySqrt1(int x) {

        if (x == 1 || x == 2) {
            return 1;
        }

        int left = 0;
        int right = x - 1;
        int mid = 0;

        while (left <= right) {
            mid = left + (right - left) / 2;

            if (x / mid == mid) {
                return mid;
            }

            Long s = Long.valueOf(mid) * Long.valueOf(mid);

            if (s > x) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        if (mid != 0 && mid > x / mid) {
            return mid - 1;
        }

        return mid;
    }


    public static void main(String[] args) {
        System.out.println("result: " + mySqrt1(2147395600));

        System.out.println(Long.valueOf(1073697800L * 1073697800L));
    }

}
