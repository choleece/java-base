package cn.choleece.base.leetcode;

public class TreeNode {

    public int val;

    public TreeNode left;

    public TreeNode right;

    public TreeNode(int x) {
        val = x;
    }

    public static TreeNode initTree() {
        TreeNode root = new TreeNode(0);

        return root;
    }
}
