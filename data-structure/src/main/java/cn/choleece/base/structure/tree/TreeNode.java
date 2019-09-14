package cn.choleece.base.structure.tree;

import java.util.*;

/**
 * 树结构 这是用"指针的形式存储的"，还有一种是用数组存储（这种需要二叉树为完全二叉树，否则就会有空间的浪费）
 *
 * 数组存储的形式为：数组下表从1开始，0空着。如果节点X存储在数组中下标为i的位置，下标为2 * i 的位置存储的就是左子节点，下标为2 * i + 1的位置存储的就是右子节点。
 * 反过来，下标为i/2的位置存储就是它的父节点。通过这种方式，我们只要知道根节点存储的位置（一般情况下，为了方便计算子节点，根节点会存储在下标为1的位置），
 * 这样就可以通过下标计算，把整棵树都串起来（采用层序遍历，根节点开始，1->2->3依次开始数，根据上述公式，就可以构造一个数出来）
 *
 * 完全二叉树仅仅会浪费一个坐标0的位置，如果不是完全二叉树，则会浪费空出来的节点的位置，所以完全的二叉数的定义要求是叶子节点那一层可以为空，而且左子树不能为空
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
     * 层序遍历（借助队列先进先出的思想），本质是一种BFS
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

    /**
     * BSP代码模版（广度优先搜索）此处的TreeNode可以换成图等数据结构
     * @param root
     */
    public static void bfs(TreeNode root) {
        if (root == null)  {
            return;
        }

        // 用队列存储将要访问的节点，因为队列有FIFO的特性，所以会保证按照广度的顺序进行
        Queue<TreeNode> queue = new LinkedList<TreeNode>();
        // 将待访问的节点放入到队列中去
        queue.add(root);

        // 存放已经访问过的节点，保证已经访问过的节点将不再继续访问
        Set<TreeNode> visitedSet = new HashSet<TreeNode>();
        visitedSet.add(root);

        while (!queue.isEmpty()) {
            // 取出队列头部的节点
            TreeNode tmpNode = queue.poll();
            // 将已访问过的节点放入访问队列中
            visitedSet.add(tmpNode);

            // 针对节点进行处理
            precessNode(tmpNode);

            // 获取节点待访问的所有节点
            List<TreeNode> nodes = generateRelatedNodes(tmpNode);

            // 将新的节点加入到队列中
            queue.addAll(nodes);
        }
    }

    /**
     * DFS模版（深度优先搜索）此处的TreeNode可以换成图等数据结构, DFS只要是利用栈FILO的思想
     * @param root
     */
    public static void dfs(TreeNode root) {
        if (root == null) {
            return;
        }

        // 用栈存储将要访问的节点，因为栈有FILO的特性，所以会保证按照深度的顺序进行
        Stack<TreeNode> stack = new Stack<TreeNode>();
        stack.add(root);

        // 存放已经访问过的节点，保证已经访问过的节点将不再继续访问
        Set<TreeNode> visitedSet = new HashSet<TreeNode>();
        visitedSet.add(root);

        while (!stack.isEmpty()) {
            // 取出队列头部的节点
            TreeNode tmpNode = stack.pop();
            // 将已访问过的节点放入访问栈中
            visitedSet.add(tmpNode);

            // 针对节点进行处理
            precessNode(tmpNode);

            // 获取节点待访问的所有节点
            List<TreeNode> nodes = generateRelatedNodes(tmpNode);

            // 将新的节点加入到栈中
            for (TreeNode node : nodes) {
                stack.push(node);
            }
        }

    }

    /**
     * DFS 递归写法 （因为递归函数本身就是一个调用栈，所以可以不用一个单独的栈）
     * @param root
     * @param visitedSet 在树这个结构里可以不需要，因为树不指回原来的节点
     */
    public static void recursionDfs(TreeNode root, Set<TreeNode> visitedSet) {
        if (root == null) {
            return;
        }
        // 存放已经访问过的节点，保证已经访问过的节点将不再继续访问
        visitedSet.add(root);

        // 针对节点进行处理
        precessNode(root);

        // 获取节点待访问的所有节点
        List<TreeNode> nodes = generateRelatedNodes(root);

        for (TreeNode node : nodes) {
            // 如果没有被访问过，那么可以继续访问
            if (!visitedSet.contains(node)) {
                recursionDfs(node, visitedSet);
            }
        }
    }

    /**
     * 针对节点进行业务处理
     * @param node
     */
    public static void precessNode(TreeNode node) {
        System.out.println("node val: " + node.val);
    }

    /**
     * 获取节点待访问的所有节点
     * @param node
     * @return
     */
    public static List<TreeNode> generateRelatedNodes(TreeNode node) {
        List<TreeNode> list = new LinkedList<TreeNode>();
        if (node.left != null) {
            list.add(node.left);
        }
        if (node.right != null) {
            list.add(node.right);
        }

        return list;
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

        System.out.println("bfs tree: ");
        bfs(root);

        System.out.println("no recursion dfs tree: ");
        dfs(root);

        System.out.println("recursion dfs tree: ");
        Set<TreeNode> visitedSet = new HashSet<TreeNode>();
        recursionDfs(root, visitedSet);
    }
}
