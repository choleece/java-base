## 递归 回溯 分治 贪心算法
[!参考](https://www.cnblogs.com/chaojunwang-ml/p/11240548.html)

### 递归 recursion

```
def recursion(level, param1, param2):

    # recursion termination 终止条件
    if MAX > level:
        print_result
        return

    # process data in current level 在这里处理逻辑
    process_data(level, data, ...)

    # drill down 重复调用
    recursion(level + 1, param1, param2)

    # reserve the current level state if needed 如果有必要，保存状态
    reserve_state(level)

```


### 回溯 backtrack
def backtrack(路径, 选择列表, result):
    if 满足结束条件:
        result.add(路径)
        return
    
    for 选择 in 选择列表:
        做选择
        backtrack(路径, 选择列表, result)
        撤销选择

### 分治 divide & conquer
def divide_conquer(problem, param1, param2, ...):
    # recursion termination
    if problem is None:
        print_result
        return

    # process_data
    data = prepare_data(problem)
    sub_problems = split_problem(problem, data)

    # conquer sub problems
    sub_result1 = divide_conquer(sub_problems[0], param1, param2, ...)
    sub_result2 = divide_conquer(sub_problems[1], param1, param2, ...)
    ...

    # process and generate the final result
    result = process_result(sub_result1, sub_result2, ...)

### 动态规划  dynamic program
1. 建立状态转移方程
2. 缓存并复用以往结果
3. 按顺序从小到大算

### DFS & BFS

```
/**
 * BSF代码模版（广度优先搜索）此处的TreeNode可以换成图等数据结构
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
```