package cn.choleece.base.leetcode.ali;

/**
 * @author choleece
 * @Description: 根据二分法保留保留小数精度
 *
 * 已知 sqrt(2)约等于 1.414，要求不用数学库，求 sqrt(2)精确到小数点后 10 位
 * @Date 2019-10-21 21:33
 **/
public class SqrtTwoTest {

    /**
     * 让其左右的值小于等于这个值
     */
    public static final double ACCURACY = 0.0000000001;

    public static double sqrt2() {
        double left = 1.4d;
        double right = 1.5d;
        double mid = (left + right) / 2;

        while (right - left > ACCURACY) {
            mid = (left + right) / 2;
            if (mid * mid > 2) {
                right = mid;
            } else {
                left = mid;
            }
        }

        return mid;
    }

    public static void main(String[] args) {
        System.out.println("sqrt2: " + sqrt2());
    }
}
