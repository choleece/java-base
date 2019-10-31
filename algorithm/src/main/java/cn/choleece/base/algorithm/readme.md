## 递归模版(recursion)

```
    def recursion(level, param1, param2):
        
        // 终止条件
        if (level > MAX_LEVEL):
            print_result
            return
        
        // 在这里处理相应的逻辑
        process_data(level, data...)
        
        // 重复递归调用
        recursion(level + 1, param1, param2...)
        
        // recursion 返回后处理的逻辑
        reserve_state(level);
```

## 分治(divide & conquer)

```
    def divide_conquer(problem, param1, param2...)
        
        // 递归终止条件
        if problem is none:
            print_result
            return
            
        // 处理相应的逻辑
        data = process_data(problem, data)
        
        // 得到子问题
        subProblems = split_problem(problem, data)
        
        // conquer sub problem
        
        subResult1 = ivide_conquer(subProblems[0], param1, param2...)
        subResult2 = divide_conquer(subProblems[1], param1, param2...)
        subResult3 = divide_conquer(subProblems[2], param1, param2...)
        ...
        
        // 处理合并生成最后的结果集
        result = process_result(subResult1, subResult2, subResult3...);
```