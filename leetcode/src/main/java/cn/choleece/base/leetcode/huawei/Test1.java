package cn.choleece.base.leetcode.huawei;

import java.util.Scanner;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-04-05 20:34
 **/
public class Test1 {
    public static void  main(String[] args){
        Scanner scanner = new Scanner(System.in);
        String str = scanner.nextLine();
        String[] words = str.split(" ");
        int len = words.length;
        System.out.println(words[len-1].length());
    }
}
