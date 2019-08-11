package cn.choleece.base.leetcode;

import java.util.Arrays;
import java.util.List;

public class BuildTree {
    static Node buildTree(List<Integer> pre_order, List<Integer> mid_order) {
        if(pre_order.size() == 0 || mid_order.size() == 0) {
            return null;
        }
        int value = pre_order.get(0);
        int idx = findRoot(mid_order,value);
        List<Integer> mLeft = mid_order.subList(0, idx);
        List<Integer> mRight = mid_order.subList(idx + 1, mid_order.size());
        List<Integer> pLeft = pre_order.subList(1, 1 + idx);
        List<Integer> pRight = pre_order.subList(idx + 1, pre_order.size());
        Node left = buildTree(pLeft, mLeft);
        Node right = buildTree(pRight, mRight);
        return new Node(value, left, right);
    }

    static int findRoot(List<Integer> tree,int value){
        for(int i  = 0;i < tree.size();i++){
            if(tree.get(i) == value){
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        List<Integer> preOrder = Arrays.asList(10, 8, 7, 9, 15, 14, 16);
        List<Integer> midOrder = Arrays.asList(7, 8, 9, 10, 14, 15, 16);

        Node root = buildTree(preOrder, midOrder);
        printTree(root);
    }

    static void printTree(Node root) {
        if (root == null) {
            return;
        }
        System.out.println(root.getVal());
        printTree(root.getLeft());
        printTree(root.getRight());
    }
}

class Node {
    private int val;

    private Node left;

    private Node right;

    public Node(int val, Node left, Node right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }
}
