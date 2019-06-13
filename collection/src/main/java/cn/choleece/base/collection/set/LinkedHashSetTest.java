package cn.choleece.base.collection.set;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 *Linked继承HashSet & 实现set,
 * LinkedHashSet拥有如下特点:
 * 跟HashSet一样，保证集合中的数不重复；拥有set所有的操作且允许值为空；非线程安全；保持插入的顺序(这个是LinkedHashSet的重要特性)
 * Created by choleece on 2019/6/13.
 */
public class LinkedHashSetTest {

    public static void main(String[] args) {
        Set<String> set = new LinkedHashSet<String>();
    }

}
