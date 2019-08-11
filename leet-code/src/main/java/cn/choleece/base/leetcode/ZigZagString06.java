package cn.choleece.base.leetcode;

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
        if (s == null || s.isEmpty() || s.length() == 0) {
            throw new IllegalArgumentException("illegal str...");
        }
        if (numRows < 1) {
            throw new IllegalArgumentException("illegal numRows...");
        }
        if (numRows == 1) {
            return s;
        }
        char[] chars = s.toCharArray();

        int x = 0, y = 0;
        int gap = 2 * (numRows - 1);

        char[][] resultChars = new char[chars.length][numRows];

        System.out.println(chars.length / gap + 1);

        for (int i = 0; i < chars.length; i++) {
            int zNum = i / gap;

            int tmp = zNum * gap + numRows;

            int mod = i % tmp;
            int discuss = i / tmp;

            if (discuss == 0) {
                x = zNum * (numRows - 1);
                y = i % gap;
            } else {
                x = zNum * (numRows - 1) + mod + 1;
                y = numRows - mod - 2;
            }

            System.out.println("zNum: " + zNum + " tmp: " + tmp);
            System.out.println("x: " + x + ", y: " + y);
            resultChars[x][y] = chars[i];
        }

        StringBuilder str = new StringBuilder();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < chars.length; j++) {
                if (resultChars[j][i] != '\0') {
                    str.append(resultChars[j][i]);
                    System.out.println(resultChars[j][i]);
                }
            }
        }
        return str.toString();
    }

    public static void main(String[] args) {
        System.out.println(convert("ABCDEFGHIJKLM", 4));
    }

}
