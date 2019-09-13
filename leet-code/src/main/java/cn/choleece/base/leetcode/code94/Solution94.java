package cn.choleece.base.leetcode.code94;

import cn.choleece.base.leetcode.TreeNode;

import java.util.LinkedList;
import java.util.List;

/**
 * Binary Tree Inorder Traversal
 */
public class Solution94 {

    public static List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> list = new LinkedList<Integer>();
        if (root == null) {
            return list;
        }

        return inOrder(list, root);
    }

    public static List<Integer> inOrder(List<Integer> list, TreeNode root) {
        if (root == null) {
            return list;
        }

        inOrder(list, root.left);
        list.add(root.val);
        inOrder(list, root.right);

        return list;
    }

    public static void main(String[] args) {

    }
}
