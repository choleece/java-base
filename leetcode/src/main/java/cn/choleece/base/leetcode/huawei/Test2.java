package cn.choleece.base.leetcode.huawei;

import java.util.Scanner;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-04-05 21:02
 **/
public class Test2 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String str = scanner.nextLine().toLowerCase();

        String s = scanner.nextLine().toLowerCase();

        char c = s.charAt(0);
        int count = 0;
        for (char c1 : str.toCharArray()) {
            if (c == c1) {
                count++;
            }
        }

        System.out.println(count);
    }

}
