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
def backtrack(choiceList, track, answer):
    // choiceList 当前可以进行的选择列表
    // track 决策路径，当前已经作出的一系列选择
    // answer 存储符合条件的决策路径

    if track is OK:
        answer.add(track)
    else:
        # choose, 选择一个choice加入track
        backtrack(choiceList, track, answer)
        # unchoose 从track中撤销上面的选择

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