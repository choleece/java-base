package cn.choleece.base.leetcode.code73;

import java.util.*;

/**
 * Given a m x n matrix, if an element is 0, set its entire row and column to 0. Do it in-place.
 *
 * 思路：记录改变过的位置，在下一层循环的时候不用在进行判断
 * @author choleece
 */
public class Solution73 {

    public static void setZeroes1(int[][] matrix) {
        if (matrix == null) {
            return;
        }

        int rows = matrix.length, columns = matrix[0].length;

        Set<String> changed = new HashSet<String>();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (matrix[i][j] == 0 && !changed.contains(i + ":" + j)) {
                    int c = columns - 1, r = rows - 1;
                    while (c >= 0) {
                        if (matrix[i][c] != 0) {
                            changed.add(i + ":" + c);
                        }
                        matrix[i][c--] = 0;
                    }

                    while (r >= 0) {
                        if (matrix[r][j] != 0) {
                            changed.add(r + ":" + j);
                        }
                        matrix[r--][j] = 0;
                    }
                }
            }
        }
    }


    public static void setZeroes(int[][] matrix) {
        if (matrix == null) {
            return;
        }

        int rows = matrix.length, columns = matrix[0].length;

        Map<Integer, List<Integer>> zeroCoordinate = new HashMap<Integer, List<Integer>>();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (matrix[i][j] == 0) {
                    List<Integer> zeroColumns = zeroCoordinate.containsKey(i) ? zeroCoordinate.get(i) : new LinkedList<Integer>();
                    zeroColumns.add(j);
                    zeroCoordinate.put(i, zeroColumns);
                }
            }
        }

        if (!zeroCoordinate.isEmpty()) {
            final Set<Map.Entry<Integer, List<Integer>>> entries = zeroCoordinate.entrySet();

            entries.forEach(entry -> {
                int row = entry.getKey();

                int c = columns - 1;
                while (c >= 0) {
                    matrix[row][c--] = 0;
                }
                List<Integer> zeroColumns = entry.getValue();

                zeroColumns.forEach(col -> {

                    int r = rows - 1;
                    while (r >= 0) {
                        matrix[r--][col] = 0;
                    }
                });
            });
        }
    }



    public static void main(String[] args) {
        int[][] matrix = {
                {0, 1, 0},
                {1, 1, 1},
                {1, 1, 1}
        };

        setZeroes(matrix);

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }

            System.out.println();
        }
    }

}
