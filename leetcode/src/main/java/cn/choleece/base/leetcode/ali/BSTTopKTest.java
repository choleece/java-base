package cn.choleece.base.leetcode.ali;

import java.util.Arrays;
import java.util.List;

/**
 * @author choleece
 * @Description: 给定一个BST 找出其中第K小的节点
 * @Date 2019-10-21 21:45
 *
 * 对于一个BST 中序遍历可以遍历出一个有序的数列出来，所以在中序列遍历中，如果在第k次出现，那么可以直接返回数字
 *
 * 可以将中序遍历的的结果放在一个数组，然后取第K个即可（当然，此举不可取）
 **/
public class BSTTopKTest {

    static class TreeNode {
        private int val;

        private TreeNode left;

        private TreeNode right;

        public TreeNode(int val) {
            this(val, null, null);
        }

        public TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    /**
     *
     * @param preOrderList
     * @param inOrderList
     * @return
     */
    public static TreeNode buildTree(List<Integer> preOrderList, List<Integer> inOrderList) {

        if (preOrderList.isEmpty() || preOrderList.isEmpty()) {
            return null;
        }

        int rootVal = preOrderList.get(0);

        int rootIdx = inOrderList.indexOf(rootVal);

        List<Integer> leftInOrderList = inOrderList.subList(0, rootIdx);

        // 注意的点是subList是左闭右开的
        List<Integer> rightInOrderList = inOrderList.subList(rootIdx + 1, inOrderList.size());

        List<Integer> leftPreOrderList = preOrderList.subList(1, rootIdx + 1);
        List<Integer> rightPreOrderList = preOrderList.subList(rootIdx + 1, preOrderList.size());

        return new TreeNode(rootVal, buildTree(leftPreOrderList, leftInOrderList), buildTree(rightPreOrderList, rightInOrderList));
    }

    public static void preOrder(TreeNode root) {

        if (root == null) {
            return;
        }

        System.out.print(root.val + "---->");
        preOrder(root.left);
        preOrder(root.right);
    }

    // 需要一个引用返回
    static class ResultType {
        private boolean found = false;

        // 递归的次数
        private int val;

        public ResultType(boolean found, int val) {
            this.found = found;
            this.val = val;
        }
    }

    public static int kthSmallest(TreeNode root, int k) {
        return kthSmallestHelper(root, k).val;
    }

    // 不好理解
    public static ResultType kthSmallestHelper(TreeNode root, int k) {
        if (root == null) {
            return new ResultType(false, 0);
        }

        ResultType leftResult = kthSmallestHelper(root.left, k);

        // 如果左子数找到
        if (leftResult.found) {
            return new ResultType(true, leftResult.val);
        }
        // 在根节点查找
        if (k - leftResult.val == 1) {
            return new ResultType(true, root.val);
        }

        // 代表走过了
        ResultType rightResult = kthSmallestHelper(root, k - leftResult.val - 1);

        if (rightResult.found) {
            return new ResultType(true, rightResult.val);
        }

        // 没找到
        return new ResultType(false, leftResult.val + 1 + rightResult.val);
    }

    public static void main(String[] args) {
        List<Integer> preOrderList = Arrays.asList(20, 10, 5, 15, 30, 25, 35);

        List<Integer> inOrderList = Arrays.asList(5, 10, 15, 20, 25, 30, 35);

        TreeNode root = buildTree(preOrderList, inOrderList);

        preOrder(root);

        System.out.println("end");

        System.out.println(preOrderList.subList(0, 3));
    }
}
