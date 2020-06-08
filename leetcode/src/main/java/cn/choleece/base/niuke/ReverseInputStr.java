package cn.choleece.base.niuke;

import java.util.Scanner;

/**
 * @author choleece
 * @Description: 反转输入的字符串
 * @Date 2020-06-08 23:15
 **/
public class ReverseInputStr {

    public static String reverseStr(String str) {
        if (str == null || str.length() ==  0) {
            return str;
        }

        char[] chars = str.toCharArray();

        StringBuffer reverseSb = new StringBuffer();
        for(int i = chars.length - 1; i >= 0; i--) {
            reverseSb.append(chars[i]);
        }

        return reverseSb.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while(scanner.hasNext()) {
            String str = scanner.nextLine();

            System.out.printf("%s\n", reverseStr(str));
        }
    }

}
