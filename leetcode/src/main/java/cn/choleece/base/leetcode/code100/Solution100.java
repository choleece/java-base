package cn.choleece.base.leetcode.code100;

import cn.choleece.base.leetcode.TreeNode;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-09-15 23:03
 **/
public class Solution100 {

    public static boolean isSameTree(TreeNode p, TreeNode q) {

        if (p == null && q == null) {
            return true;
        }

        if (!isNodeEqual(p, q)) {
            return false;
        }

        Queue<TreeNode> pQueue = new LinkedList<>();
        pQueue.offer(p);
        Queue<TreeNode> qQueue = new LinkedList<>();
        qQueue.offer(q);

        while (!pQueue.isEmpty() && !qQueue.isEmpty()) {
            TreeNode pp = pQueue.poll();
            TreeNode qq = qQueue.poll();

            if (!isNodeEqual(pp.left, qq.left)) {
                return false;
            }

            if (pp.left != null) {
                pQueue.offer(pp.left);
                qQueue.offer(qq.left);
            }

            if (!isNodeEqual(pp.right, qq.right)) {
                return false;
            }

            if (qq.right != null) {
                pQueue.offer(pp.right);
                qQueue.offer(qq.right);
            }
        }

        return true;
    }

    public static boolean isNodeEqual(TreeNode p, TreeNode q) {
        return (p == null && q == null) || (p != null && q != null && p.val == q.val);
    }

    public static boolean isSameTreeWithRecursion(TreeNode p, TreeNode q) {
        if (p == null && q == null) {
            return true;
        }

        if (p == null && q != null || p != null && q == null) {
            return false;
        }

        if (p.val != q.val) {
            return false;
        }

        return isNodeEqual(p.left, q.left) && isNodeEqual(p.right, q.right);
    }

    public static void main(String[] args) {
        TreeNode p = TreeNode.initTree();

        p.left = new TreeNode(1);
        p.right = new TreeNode(2);

        TreeNode q = TreeNode.initTree();
        q.right = new TreeNode(2);

        System.out.println(isSameTreeWithRecursion(p, q));
    }

}
