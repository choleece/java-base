package cn.choleece.base.niuke;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author choleece
 * @Description: 梅花桩
 * @Date 2020-06-09 21:14
 **/
public class Redraiment {

    public static int redraiment(int num, int[] input, List pResult) {

        int[] dp = new int[num];
        int max = 0;
        for(int i = 0; i < input.length; i++) {
            dp[i] = 1;
            // 这里利用动态规划从前往后找
            for (int j = 0; j < i; j++) {
                if (input[j] < input[i]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }

            if (dp[i] > max) {
                max = dp[i];
            }
        }

        pResult.add(max);
        return max;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            int num = scanner.nextInt();
            int[] input = new int[num];
            for(int i = 0; i < num; i++) {
                input[i] = scanner.nextInt();
            }

            List<Integer> pResult = new ArrayList<>();
            System.out.printf("%d", redraiment(num, input, pResult));
        }
    }

}
