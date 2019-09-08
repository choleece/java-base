package cn.choleece.base.structure.tree;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 树结构
 * @author choleece
 */
public class TreeNode {

    public int val;

    private TreeNode left;

    private TreeNode right;

    public TreeNode(int val) {
        this.val = val;
    }

    public TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }

    public static TreeNode buildTree(List<Integer> preOrder, List<Integer> inOrder) {
        if (preOrder.isEmpty() || inOrder.isEmpty()) {
            return null;
        }

        int rootVal = preOrder.get(0);
        int rootIdx = inOrder.indexOf(rootVal);

        List<Integer> inOrderLeft = inOrder.subList(0, rootIdx);
        List<Integer> inOrderRight = inOrder.subList(rootIdx + 1, inOrder.size());

        List<Integer> preOrderLeft = preOrder.subList(1, rootIdx + 1);
        List<Integer> preOrderRight = preOrder.subList(rootIdx + 1, preOrder.size());

        return new TreeNode(rootVal, buildTree(preOrderLeft, inOrderLeft), buildTree(preOrderRight, inOrderRight));
    }

    /**
     * 前序遍历
     */
    public static void preOrder(TreeNode root) {
        if (root == null) {
            return;
        }

        System.out.println("val: " + root.val);
        preOrder(root.left);
        preOrder(root.right);
    }

    /**
     * 中序遍历 遍历完之后是一个有序的
     */
    public static void inOrder(TreeNode root) {
        if (root == null) {
            return;
        }

        inOrder(root.left);
        System.out.println("val: " + root.val);
        inOrder(root.right);
    }

    /**
     * 后续遍历
     */
    public static void postOrder(TreeNode root) {
        if (root == null) {
            return;
        }

        postOrder(root.left);
        postOrder(root.right);
        System.out.println("val: " + root.val);

    }

    /**
     * 层序遍历（借助队列先进先出的思想）（递归函数本身是一个栈）
     */
    public static void levelOrder(TreeNode root) {
        if (root == null) {
            return;
        }
        Queue<TreeNode> queue = new LinkedList<TreeNode>();
        queue.add(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            System.out.println("val: " + node.val);
            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
        }
    }

    public static void main(String[] args) {
//        TreeNode root = new TreeNode(20, new TreeNode(10, new TreeNode(5), new TreeNode(15)), new TreeNode(30, new TreeNode(25), new TreeNode(35)));

        TreeNode root = buildTree(Arrays.asList(20, 10, 5, 15, 30, 25, 35), Arrays.asList(5, 10, 15, 20, 25, 30, 35));

        System.out.println("preOrder:");
        preOrder(root);

        System.out.println("inOrder:");
        inOrder(root);

        System.out.println("postOrder:");
        postOrder(root);

        System.out.println("levelOrder: ");
        levelOrder(root);
    }
}
