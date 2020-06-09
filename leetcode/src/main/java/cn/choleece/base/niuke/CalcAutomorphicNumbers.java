package cn.choleece.base.niuke;

import java.util.Scanner;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-06-09 22:05
 **/
public class CalcAutomorphicNumbers {

    public static int calcAutomorphicNumbers(int n) {

        if (n < 0) {
            return 0;
        }

        int count = 0;

        for(int i = 0; i <= n; i++) {
            if (String.valueOf(i * i).endsWith(String.valueOf(i))) {
                count++;
            }
        }

        System.out.println(count);
        return count;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while(scanner.hasNext()) {
            int num = scanner.nextInt();

            calcAutomorphicNumbers(num);
        }
    }

}
