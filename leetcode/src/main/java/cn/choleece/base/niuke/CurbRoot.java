package cn.choleece.base.niuke;

import java.util.Scanner;

/**
 * @author choleece
 * @Description: 开3次方
 * @Date 2020-06-08 22:54
 **/
public class CurbRoot {

    public static double getCurbRoot(double input) {

        boolean isNegative = false;
        double left = 0;

        if (input < 0) {
            isNegative = true;
            input = -1 * input;
        }
        double right = input;

        double mid = (left + right) / 2;
        double multiply = mid * mid * mid;

        while(multiply - input > 0.001 || multiply < input) {
            multiply = mid * mid * mid;
            if (multiply == input) {
                break;
            } else if (multiply > input) {
                right = mid;
                mid = (mid + left) / 2;
            } else {
                left = mid;
                mid = (mid + right) / 2;
            }
        }

        return mid * (isNegative ? -1 : 1);
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            double input = scanner.nextDouble();

            System.out.printf("%.1f\n", getCurbRoot(input));
        }
    }

}
