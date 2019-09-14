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

    /**
     * 支持重复数据的二叉查找树前面讲二叉查找树的时候，我们默认树中节点存储的都是数字。很多时候，在实际的软件开发中，我们在二叉查找树中存储的，是一个包含很多字段的对象。
     * 我们利用对象的某个字段作为键值（key）来构建二叉查找树。我们把对象中的其他字段叫作卫星数据。
     * 前面我们讲的二叉查找树的操作，针对的都是不存在键值相同的情况。那如果存储的两个对象键值相同，这种情况该怎么处理呢？
     * 我这里有两种解决方法。
     * 第一种方法比较容易。二叉查找树中每一个节点不仅会存储一个数据，因此我们通过链表和支持动态扩容的数组等数据结构，把值相同的数据都存储在同一个节点上。
     * 第二种方法比较不好理解，不过更加优雅。每个节点仍然只存储一个数据。在查找插入位置的过程中，如果碰到一个节点的值，与要插入数据的值相同，
     * 我们就将这个要插入的数据放到这个节点的右子树，也就是说，把这个新插入的数据当作大于这个节
     * @param t
     * @return
     */
    public BinaryNode<T> find(T t) {
        if (root == null) {
            return null;
        }

        BinaryNode<T> p = root;
        while (p != null) {
            int compareResult = t.compareTo(p.e);

            if (compareResult == 0) {
                return p;
            }

            p = compareResult > 0 ? p.right : p.left;
        }

        return p;
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

    /**
     * 如果空，直接返回，如果是叶子节点，直接删除，如果只有一个孩子，直接将孩子节点替换掉当前要删除对节点，如果右两个孩子，用右子树最小对节点（也即右子树最左边对节点）将内容替换掉，然后删除最小节点
     * 第一种情况是，如果要删除的节点没有子节点，我们只需要直接将父节点中，指向要删除节点的指针置为null。
     * 第二种情况是，如果要删除的节点只有一个子节点（只有左子节点或者右子节点），我们只需要更新父节点中，指向要删除节点的指针，让它指向要删除节点的子节点就可以了。
     * 第三种情况是，如果要删除的节点有两个子节点，这就比较复杂了。我们需要找到这个节点的右子树中的最小节点，把它替换到要删除的节点上。然后再删除掉这个最小节点，因为最小节点肯定没有左子节点（如果有左子结点，那就不是最小节点了），所以，我们可以应用上面两条规则来删除这个最小节点。
     *
     * 实际上，关于二叉查找树的删除操作，还有个非常简单、取巧的方法，就是单纯将要删除的节点标记为“已删除”，
     * 但是并不真正从树中将这个节点去掉。这样原本删除的节点还需要存储在内存中，比较浪费内存空间，但是删除操作就变得简单了很多。
     * 而且，这种处理方法也并没有
     * @param t
     */
    public void remove(T t) {
        root = remove(t, root);
    }

    private BinaryNode<T> remove(T t, BinaryNode<T> root) {
        if (root == null) {
            return null;
        }

        int compareResult = t.compareTo(root.e);
        if (compareResult > 0) {
            root.right = remove(t, root.right);
        } else if (compareResult < 0) {
            root.left = remove(t, root.left);
        } else if (root.left != null && root.right != null) {
            BinaryNode<T> node = findMin(root.right);
            root.e = node.e;
            root.right = remove(node.e, root.right);
        } else {
            root = root.left != null ? root.left : root.right;
        }

        return root;
    }

    /**
     * 利用上述的思路，先找到节点，再看节点有几个孩子节点
     * @param t
     * @return
     */
    private BinaryNode<T> removeWithoutRecursion(T t) {
        if (root == null) {
            return null;
        }

        BinaryNode<T> p = root, pp = null;

        while (p != null) {

            int compareResult = t.compareTo(p.e);

            if (compareResult == 0) {
                break;
            }
            pp = p;
            p = compareResult > 0 ? p.right : p.left;
        }

        if (p == null) {
            return null;
        }

        // 两个孩子节点
        if (p.left != null && p.right != null) {

            BinaryNode<T> minP = p.right;
            BinaryNode<T> minPP = p;

            while (minP.left != null) {
                minPP = p;
                minP = minP.left;
            }

            T tmpVal = p.e;

            // 内容用右子树的最小值替换
            p.e = minP.e;

            // 将删除的值保存在右子树最小值里，走下边的删除操作
            minP.e = tmpVal;

            p = minP;
            pp = minPP;
        }

        // 叶子节点 或者有且仅有一个孩子节点
        if (p.left == null || p.right == null) {
            // 说明是根节点
            if (pp == null) {
                BinaryNode<T> delNode = p;
                p = null;
                return delNode;
            } else if (pp.left == p) {
                pp.left = p.left == null ? p.right : p.left;
            } else if (pp.right == p) {
                pp.right = p.right == null ? p.left : p.right;
            }
        }

        return p;
    }

    public void printTree() {
        printTree(root);
    }

    private void printTree(BinaryNode<T> root) {

    }
}
