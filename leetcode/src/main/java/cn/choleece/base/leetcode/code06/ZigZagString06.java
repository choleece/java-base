package cn.choleece.base.leetcode.code06;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 将一个给定字符串根据给定的行数，以从上往下、从左到右进行 Z 字形排列。
 *
 * 比如输入字符串为 "LEETCODEISHIRING" 行数为 3 时，排列如下：
 *
 * L   C   I   R
 * E T O E S I I G
 * E   D   H   N
 * 之后，你的输出需要从左往右逐行读取，产生出一个新的字符串，比如："LCIRETOESIIGEDHN"。
 *
 */
public class ZigZagString06 {

    public static String convert(String s, int numRows) {
        if (s == null || s.length() == 0 || numRows <= 0) {
            throw new IllegalArgumentException("请传入合法的字符串");
        }
        if (numRows == 1) {
            return s;
        }
        List<StringBuffer> rows = new ArrayList<StringBuffer>();
        for (int i = 0; i < Math.min(numRows, s.length()); i++) {
            rows.add(new StringBuffer());
        }
        char[] chars = s.toCharArray();

        int rown = Math.min(numRows, s.length());
        for (int i = 1; i <= chars.length ; i++) {
            char c = chars[i - 1];
            int remain = i % (2 * rown - 2);
            if (remain == 0) {
                rows.get(1).append(c);
            } else if (remain <= rown) {
                rows.get(remain - 1).append(c);
            } else {
                rows.get(2 * rown - remain - 1).append(c);
            }
            System.out.println(remain);
        }

        StringBuilder res = new StringBuilder("");
        for (StringBuffer sb : rows) {
            res.append(sb);
        }

        return res.toString();
    }

    public static void main(String[] args) {
        System.out.println(convert("PAYPALISHIRING", 4));
    }

}
