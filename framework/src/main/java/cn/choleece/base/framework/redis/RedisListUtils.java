package cn.choleece.base.framework.redis;

import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * @author choleece
 * @Description: Redis List 结构常用方法
 * @Date 2020-01-04 23:42
 * List 底层是一个双向链表，有头指针 + 尾指针
 **/
public class RedisListUtils {
    static Jedis jedis = RedisConfig.jedis;

    public static void main(String[] args) {
//        lpush("name", "chao");

        System.out.println(blpop("name", "sex", 0));
    }

    /**
     * 从左push value 到栈中
     * @param key
     * @param val
     * @return
     */
    public static Long lpush(String key, String val) {
        Long r = jedis.lpush(key, val);

        System.out.println("r: " + r);

        return r;
    }

    /**
     * 当key 存在时，hx命令什么都不做
     * @param key
     * @param val
     * @return
     */
    public static Long lpushHx(String key, String val) {
        Long r = jedis.lpush(key, val);

        return r;
    }

    /**
     * 从左弹出内容，返回弹出的内容
     * @param key
     * @return
     */
    public static String lpop(String key) {
        String r = jedis.lpop(key);

        System.out.println("r: " + r);

        return r;
    }

    /**
     * 从右push value 到栈中
     * @param key
     * @param val
     * @return
     */
    public static Long rpush(String key, String val) {
        Long r = jedis.rpush(key, val);

        System.out.println("r: " + r);

        return r;
    }

    /**
     * 从右弹出内容，返回弹出的内容
     * @param key
     * @return
     */
    public static String rpop(String key) {
        String r = jedis.rpop(key);

        System.out.println("r: " + r);

        return r;
    }

    /**
     * 返回key的值的个数
     * @param key
     * @return
     */
    public static Long llen(String key) {
        Long r = jedis.llen(key);

        System.out.println("r: " + r);

        return r;
    }

    /**
     * 返回key从start 到 end的内容
     * @param key
     * @param start
     * @param end
     * @return
     */
    public static List<String> lrange(String key, long start, long end) {
        List<String> list = jedis.lrange(key, start, end);

        System.out.println("list: " + list);

        return list;
    }

    /**
     * 返回指定位置idx的值，从0开始，0带代表第一个， -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素
     * @param key
     * @param idx
     * @return
     */
    public static String lindex(String key, long idx) {
        return jedis.lindex(key, idx);
    }

    /**
     * 在key的list里，进行beforeKey的po位置进行插入操作
     * @param key
     * @param po
     * @param beforeVal
     * @param val
     * @return
     * LINSERT mylist BEFORE "World" "There"
     */
    public static Long linsert(String key, BinaryClient.LIST_POSITION po, String beforeVal, String val) {
        return jedis.linsert(key, po, beforeVal, val);
    }

    /**
     * 在指定超时的时间范围内，去弹出key1,key2的值，如果key的值都为空，则会一直阻塞或者等待超时； 否则会返回第一部位空的key + key对应的val组成的数组，0的位置为key, 1的位置为val
     * @param key1
     * @param key2
     * @param timeout
     * @return
     */
    public static List<String> blpop(String key1, String key2, int timeout) {
        return jedis.blpop(timeout, key1, key2);
    }
}
