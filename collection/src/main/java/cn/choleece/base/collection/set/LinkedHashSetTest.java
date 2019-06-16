package cn.choleece.base.collection.set;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *Linked继承HashSet & 实现set,
 * LinkedHashSet拥有如下特点:
 * 跟HashSet一样，保证集合中的数不重复；拥有set所有的操作且允许值为空；非线程安全；保持插入的顺序(这个是LinkedHashSet的重要特性)
 * Created by choleece on 2019/6/13.
 */
public class LinkedHashSetTest {

    /**
     * 调用的是继承HashSet的构造函数，HashSet的构造函数有个参数，叫dummy，设置为true，表示构造一个LinkedHashMap实例
     */
    public static void testConstructLinkedHashSet() {

        /**
         * 无参构造函数，
         */
        new LinkedHashSet<>();

        /**
         * 指定Capacity的构造函数
         */
        new LinkedHashSet<>(8);

        /**
         * 指定Capacity & LoadFactor的构造函数
         */
        new LinkedHashSet<>(8, 1);

        /**
         * 指定初始化集合的构造函数
         */
        new LinkedHashSet<String>(new HashSet<String>(){{add("choleece");}});


    }

    public static void main(String[] args) {
        Set<String> set = new LinkedHashSet<String>();

        set.add("One");
        set.add("Two");
        set.add("Three");
        set.add("Four");
        set.add("Five");

        set.add("Five");

        Iterator<String> i = set.iterator();
        while(i.hasNext()) {
            System.out.println(i.next());
        }
    }

}
