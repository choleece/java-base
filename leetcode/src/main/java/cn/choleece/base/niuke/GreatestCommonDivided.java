package cn.choleece.base.niuke;

import java.util.Scanner;

/**
 * @author choleece
 * @Description: 最大公约数
 * @Date 2020-06-08 22:18
 **/
public class GreatestCommonDivided {

    /**
     * 计算两个数的最大公约数
     * @param a
     * @param b
     * @return
     */
    public static int greatestCommonDivided(int a, int b) {
        if (a % b == 0) {
            return b;
        }

        return greatestCommonDivided(b, a % b);
    }

    /**
     * 最小公倍数
     * @param a
     * @param b
     * @return
     */
    public static int leastCommonMultiply(int a, int b) {

        return (a * b) / greatestCommonDivided(a, b);
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        while(scan.hasNext()) {
            int a = scan.nextInt();
            int b = scan.nextInt();

            System.out.println(leastCommonMultiply(a, b));
        }
    }

}
