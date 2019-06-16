package cn.choleece.base.collection.map;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LinkedHashMap是一个HashMap加List(底层是个双链表)的实现，其拥有的特性跟HashMap一样，存储结构依旧也没变，只是把每个节点的关系都维护在一个双向链表上，其能保持一个插入顺序
 * Created by choleece on 2019/6/16.
 */
public class LinkedHashMapTest {

    /**
     * 不论哪种构造函数，其底层都是调用了HashMap的构造函数
     * /**
     * HashMap.Node subclass for normal LinkedHashMap entries.
     *
     * LinkedHashMap维护链关系的结构，比HashMap的Node多了一个向前、向后的指针
     *static class Entry<K,V> extends HashMap.Node<K,V> {
     *  LinkedHashMap.Entry<K,V> before, after;
     *  Entry(int hash, K key, V value, HashMap.Node<K,V> next) {
     *       super(hash, key, value, next);
     *  }
     *}
     */
    public static void testConstructLinkedHashMap() {

        /**
         * 无参构造函数
         */
        new LinkedHashMap<>();

        /**
         * 指定数组大小构造函数
         */
        new LinkedHashMap<>(10);

        /**
         * 指定数组大小及负载因子构造函数
         */
        new LinkedHashMap<>(10, 1);

        /**
         * 初始化一个map
         */
        new LinkedHashMap<>(new HashMap<String, String>(){{put("name", "choleece");}});
    }

    public static void main(String[] args) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("100", "choleece");
        map.put("102", "choleece");
        map.put("101", "bing");

        System.out.println("----------entry map----------");
        for (Map.Entry entry : map.entrySet()) {
            System.out.println(entry);
        }

        System.out.println("fetching the keys: " + map.keySet());

        System.out.println("fetching the values: " + map.values());

        System.out.println("fetching the key-value pairs: " + map.entrySet());

        map.remove("101");

        System.out.println("remove data: " + map);
    }

}
