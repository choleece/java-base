package cn.choleece.base.framework.redis;

import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;

/**
 * @author choleece
 * @Description: 集合操作
 * @Date 2020-01-06 21:58
 **/
public class RedisSetUtils {

    static Jedis jedis = RedisConfig.jedis;

    /**
     * 集合 保留不重复的值
     * @param key
     * @param val
     * @return
     */
    public static Long sadd(String key, String val) {
        return jedis.sadd(key, val);
    }

    /**
     * 从集合key中移除val1, val2
     * @param key
     * @param val1
     * @param val2
     * @return
     */
    public static Long srem(String key, String val1, String val2) {
        return jedis.srem(key, val1, val2);
    }

    /**
     * 随机移除一个元素，并返回这个元素
     * @param key
     * @return
     */
    public static String spop(String key) {
        return jedis.spop(key);
    }

    /**
     * 随机从key中返回count 个元素，如果count < 0，则List 中会存在重复的元素
     * @param key
     * @param count
     * @return
     */
    public static List<String> sRandomMember(String key, int count) {
        return jedis.srandmember(key, count);
    }

    /**
     * 随机从集合中返回一个元素
     * @param key
     * @return
     */
    public static String sRandomMember(String key) {
        return jedis.srandmember(key);
    }

    /**
     * 从source key 中将val移动到destinationKey中，当destinationKey存在val时，只是简单的将val从source key中移除
     * @param sourceKey
     * @param destinationKey
     * @param val
     * @return
     */
    public static Long smove(String sourceKey, String destinationKey, String val) {
        return jedis.smove(sourceKey, destinationKey, val);
    }

    /**
     * 判断key中是否存在元素val
     * @param key
     * @param val
     * @return
     */
    public static Boolean sIsMember(String key, String val) {
        return jedis.sismember(key, val);
    }

    /**
     * 返回key的集合
     * @param key
     * @return
     */
    public static Set<String> smembers(String key) {
        return jedis.smembers(key);
    }

    /**
     * 统计key集合的元素个数
     * @param key
     * @return
     */
    public static Long scard(String key) {
        return jedis.scard(key);
    }

    /**
     * 返回包含在key1中，但是不包含在key2,key3集合中的元素
     * @param key1
     * @param key2
     * @param key3
     * @return
     */
    public static Set<String> sdiff(String key1, String key2, String key3) {
        return jedis.sdiff(key1, key2, key3);
    }

    /**
     * 针对key1, key2取diff操作，然后将diff的结果集放入destination key中（如果存在，则覆盖）
     * @param destinationKey
     * @param key1
     * @param key2
     * @return
     */
    public static Long sdiffStore(String destinationKey, String key1, String key2) {
        return jedis.sdiffstore(destinationKey, key1, key2);
    }

    /**
     * 将多个key的结果集取交集
     * @param key1
     * @param key2
     * @param key3
     * @return
     */
    public static Set<String> sinter(String key1, String key2, String key3) {
        return jedis.sinter(key1, key2, key3);
    }

    /**
     * 将key1, key2取的交集的结果集存入destination key中
     * @param destinationKey
     * @param key1
     * @param key2
     * @return
     */
    public static Long sinterStore(String destinationKey, String key1, String key2) {
        return jedis.sinterstore(destinationKey, key1, key2);
    }

    /**
     * 集合key1, key2取并集
     * @param key1
     * @param key2
     * @return
     */
    public static Set<String> sunion(String key1, String key2) {
        return jedis.sunion(key1, key2);
    }

    /**
     * 将key1, key2取并集得到的结果集存入destination key中
     * @param destinationKey
     * @param key1
     * @param key2
     * @return
     */
    public static Long sunionStore(String destinationKey, String key1, String key2) {
        return jedis.sunionstore(destinationKey, key1, key2);
    }
}
