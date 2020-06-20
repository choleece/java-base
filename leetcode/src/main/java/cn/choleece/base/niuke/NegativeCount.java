package cn.choleece.base.niuke;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;

/**
 * @author choleece
 * @Description: 负数统计
 * @Date 2020-06-17 23:40
 **/
public class NegativeCount {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        int negativeCount = 0;
        int positiveCount = 0;
        double positiveSum = 0;
        while(scanner.hasNextInt()) {
            int num = scanner.nextInt();
            if (num < 0) {
                negativeCount++;
            } else {
                positiveCount++;
                positiveSum += num;
            }
        }

        System.out.println(negativeCount);

        if (positiveCount == 0) {
            System.out.println(0.0);
        } else {
            System.out.println(new BigDecimal(String.valueOf(positiveSum)).divide(new BigDecimal(positiveCount))
                    .setScale(1, RoundingMode.DOWN));
        }
    }

}
