package cn.choleece.base.jdk.collection.set;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * HashSet常常被用来创建一个集合，它继承了AbstractSet 实现了Set接口
 * HashSet的特点是集合中不存在相同的值；允许存储空值；非线程安全；不保证插入顺序
 * 读HashSet的源码会发现，其底层实现是利用HashMap实现的
 * Created by choleece on 2019/6/13.
 */
public class HashSetTest {

    public static void testHashSetConstruct() {

        /**
         * 无参构造函数，底层实现是直接new HashMap();
         */
        Set<String> noParamSet = new HashSet<>();

        /**
         * 带初始大小的构造函数，底层实现是new HashMap(int capacity)
         */
        Set<String> initCapacitySet = new HashSet<>(8);

        /**
         * 带集合的构造函数，先new HashMap(int initialCapacity),然后执行addAll(Collection c)
         */
        Set<String> initCollectionSet = new HashSet<>(new LinkedList<String>() {{
            add("bing");
            add("choleece");
        }});
    }

    /**
     * HashSet的一切操作，都是基于HashMap来做的
     * @param args
     */
    public static void main(String[] args) {
        Set<String> set = new HashSet<>();

        /**
         * 执行 map.put(e, PRESENT)
         */
        set.add("name");

        /**
         * 遍历执行的add操作
         */
        set.addAll(new LinkedList<String>() {
            {
                add("choleece");
            }
        });

        /**
         * 执行map.containsKey("name")
         */
        set.contains("name");

        /**
         * 遍历执行contains(o)操作
         */
        set.containsAll(new LinkedList<String>() {
            {
                add("choleece");
            }
        });

        /**
         * 执行 map.remove(o)
         */
        set.remove("name");

        /**
         * 遍历执行remove
         */
        set.removeAll(new LinkedList<String>() {
            {
                add("choleece");
            }
        });

        /**
         * 执行map.clear
         */
        set.clear();

        /**
         *  执行 map.size();
         */
        set.size();

        /**
         * 执行map.isEmpty()
         */
        set.isEmpty();

    }
}
