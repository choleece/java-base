package cn.choleece.base.structure.tree;

import java.util.Stack;

/**
 * BST（Binary Search Tree）二叉查找树特点 左子树永远小于跟节点 右子树永远大于跟节点
 * @param <T>
 */
public class BinarySearchTree<T extends Comparable<? super T>> {

    /**
     * BST 节点
     * @param <T>
     */
    public static class BinaryNode<T> {
        T e;
        BinaryNode<T> left;
        BinaryNode<T> right;

        public BinaryNode(T e) {
            this.e = e;
        }

        public BinaryNode(T e, BinaryNode<T> left, BinaryNode<T> right) {
            this.e = e;
            this.left = left;
            this.right = right;
        }
    }

    private BinaryNode<T> root;

    /**
     * constructor
     */
    public BinarySearchTree() {
        root = null;
    }

    public void makeEmpty() {
        root = null;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public boolean contains(T t) {
        return contains(t, root);
    }

    private boolean contains(T t, BinaryNode<T> root) {
        if (root == null) {
            return false;
        }

        // 通过比较，判断是否在当前节点上
        int compareResult = t.compareTo(root.e);

        // 如果大于当前节点，根据BST定义，去右子树上查询，小于当前节点，去左子树上查询
        if (compareResult > 0) {
            contains(t, root.right);
        } else if (compareResult < 0) {
            contains(t, root.left);
        }
        return true;
    }

    /**
     * 不使用递归来进行判断
     * @param t
     * @param root
     * @return
     */
    private boolean containsWithoutRecursion(T t, BinaryNode<T> root) {
        if (root == null) {
            return false;
        }

        Stack<BinaryNode<T>> stack = new Stack<BinaryNode<T>>();
        stack.push(root);

        while (!stack.isEmpty()) {
            BinaryNode<T> node = stack.pop();
            int compareResult = t.compareTo(node.e);

            if (compareResult == 0) {
                return true;
            }

            stack.push(compareResult > 0 ? root.right : root.left);
        }
        return false;
    }

    public T findMin() {
        if (isEmpty()) {
            throw new IllegalArgumentException("tree is empty");
        }

        return findMin(root).e;
    }

    private BinaryNode<T> findMin(BinaryNode<T> root) {
        if (root == null || root.left == null) {
            return root;
        }

        return findMin(root.left);
    }

    private BinaryNode<T> findMinWithoutRecursion(BinaryNode<T> root) {
        BinaryNode<T> node = root;
        while (node != null && node.left != null) {
            node = node.left;
        }
        return node;
    }

    public T findMax() {
        if (isEmpty()) {
            throw new IllegalArgumentException("tree is empty");
        }

        return findMax(root).e;
    }

    private BinaryNode<T> findMax(BinaryNode<T> root) {
        if (root == null || root.right == null) {
            return root;
        }
        return findMax(root.right);
    }

    private BinaryNode<T> findMaxWithoutRecursion(BinaryNode<T> root) {
        BinaryNode<T> node = root;
        while (node != null && node.right != null) {
            node = node.right;
        }
        return node;
    }

    public void insert(T t) {
        root = insert(t, root);
    }

    private BinaryNode<T> insert(T t, BinaryNode<T> root) {
        if (root == null) {
            root = new BinaryNode<T>(t);
        }

        int compareResult = t.compareTo(root.e);

        if (compareResult > 0) {
            root.right = insert(t, root.right);
        } else if (compareResult < 0) {
            root.left = insert(t, root.left);
        } else {
            // equals to root
        }
        return root;
    }

    public void remove(T t) {
        root = remove(t, root);
    }

    private BinaryNode<T> remove(T t, BinaryNode<T> root) {
        return root;
    }

    public void printTree() {
        printTree(root);
    }

    private void printTree(BinaryNode<T> root) {

    }
}
