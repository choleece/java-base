package cn.choleece.base.collection.map;

import java.util.HashMap;
import java.util.Map;

/**
 * Map是一个Key-Value键值对类型对存储结构，一个Map包含一个唯一对Key，即Key不能重复，但是Value可以重复
 * Map的键和值都允许为空，并且不保证插入顺序，其底层数据结构为 数组 + 链表 + 红黑树（防止链表太长影响查询效率）
 * HashMap默认的初始大小为16， 负载因子为0.75
 * Created by choleece on 2019/6/13.
 */
public class HashMapTest {

    int a;

    public static void testConstructorMap() {

        /**
         * 无参构造函数，初始大小为16, 负载因子为0.75，其所有的参数都是默认的
         */
        Map<String, String> map = new HashMap<>();

        /**
         * 给定默认大小initialCapacity, 系统返回一个2的指数，这个数 >= initialCapacity ,且离initialCapacity最近，作为其初始大小, 比如指定5，会返回8
         */
        Map<String, String> initCapactyMap = new HashMap<>(5);

        /**
         * 给定默认大小initialCapacity & loadFactor(0< loadFactor <= 1, default is 0.75)
         */
        Map<String, String> initCapcityLoadFactor = new HashMap<>(5, 1);

        /**
         * 先初始化一个默认大小，然后执行putMapEntries()操作
         */
        Map<String, String> initMap = new HashMap<>(new HashMap<String, String>(){
            {
                put("name", "choleece");
            }
        });
    }

    public static void testPut() {
        Map<String, String> map = new HashMap<String, String>(8);

        map.put("name", "choleece");

        System.out.println(map.toString());

        /**
         *
         接下来，一起分析下map.put(K k, V v) 所进行的过程
         public V put(K key, V value) {
           return putVal(hash(key), key, value, false, true);
         }
         其会执行putVal() 操作，执行putVal之前，会先对key进行hash计算

         static final int hash(Object key) {
            int h;
            return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
         }

         如果key为空，默认返回0， 如果key不为空，则取key的hashCode,
         */

    }

    static final int tableSizeFor(int cap) {
        int n = cap;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= (1 << 30)) ? (1 << 30) : n + 1;
    }

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>(2);

        map.put("name", "choleece");

        map.put("name", "bing");

        map.containsKey("name");

        map.containsValue("value");

        map.remove("name");

        map.clear();

        System.out.println(map);
        System.out.println();
    }
}
