# AVL

https://zh.wikipedia.org/wiki/AVL%E6%A0%91

# 旋转有四种情况：
  1. 左孩子的左孩子 右旋
  2. 右孩子的右孩子 左旋
  3. 左孩子的右孩子 先左旋，再右旋
  4. 右孩子的左孩子 先右旋，再左旋
  
  **对一棵树进行旋转时，这棵树的根节点是被旋转的两棵子树的父节点，称为旋转时的根（英语：root）；如果节点在旋转后会成为新的父节点，则该节点为旋转时的转轴（英语：pivot）**
  
  [!详情见图](https://upload.wikimedia.org/wikipedia/commons/c/c7/Tree_Rebalancing.png)