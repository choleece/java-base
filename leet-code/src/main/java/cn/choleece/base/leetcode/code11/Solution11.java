package cn.choleece.base.leetcode.code11;

public class Solution11 {

    public static int maxArea1(int[] height) {
        int maxArea = 0;
        if (height.length < 2) {
            return maxArea;
        }

        for (int i = 1; i <= height.length; i++) {
            for (int j = i + 1; j <= height.length; j++) {
                int area = (j - i) * Math.min(height[i - 1], height[j - 1]);
                maxArea = Math.max(maxArea, area);
            }
        }
        return maxArea;
    }

    public static int maxArea(int[] height) {
        int maxArea = 0;
        if (height.length < 2) {
            return maxArea;
        }

        int left = 1, right = height.length;
        while (left <= right) {
            maxArea = Math.max(maxArea, (right - left) * Math.min(height[left - 1], height[right - 1]));
            if (height[left - 1] >= height[right - 1]) {
                right--;
            } else {
                left++;
            }
        }

        return maxArea;
    }

    public static void main(String[] args) {
        int[] height = {1, 8, 6, 2, 5, 4, 8, 3, 7};

        System.out.println("max area: " + maxArea(height));
    }
}
