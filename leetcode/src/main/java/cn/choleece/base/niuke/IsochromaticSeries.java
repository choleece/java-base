package cn.choleece.base.niuke;

import java.util.Scanner;

/**
 * @author choleece
 * @Description: 等差数列
 * @Date 2020-06-09 21:57
 **/
public class IsochromaticSeries {

    public static int isochromaticSeries(int num) {
        if (num < 1) {
            return -1;
        }

        System.out.println((3 * num + 1) * num / 2);

        return 0;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while(scanner.hasNext()) {
            int num = scanner.nextInt();

            isochromaticSeries(num);
        }
    }

}
