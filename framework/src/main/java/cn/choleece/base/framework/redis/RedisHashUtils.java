package cn.choleece.base.framework.redis;

import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author choleece
 * @Description: Redis Hash 相关操作
 * @Date 2020-01-08 22:53
 **/
public class RedisHashUtils {

    static Jedis jedis = RedisConfig.jedis;

    /**
     * 在key的hash 中，是否存在field字段
     * @param key
     * @param field
     * @return
     */
    public static Boolean hexist(String key, String field) {
        return jedis.hexists(key, field);
    }

    /**
     * 删除key里，属性为field1, field2的值
     * @param key
     * @param field1
     * @param field2
     * @return
     */
    public static Long hdel(String key, String field1, String field2) {
        return jedis.hdel(key, field1, field2);
    }

    /**
     * 获取key 里field的值
     * @param key
     * @param field
     * @return
     */
    public static String hget(String key, String field) {
        return jedis.hget(key, field);
    }

    /**
     * 获取key的所有值，返回一个map对象
     * @param key
     * @return
     */
    public static Map<String, String> hgetAll(String key) {
        return jedis.hgetAll(key);
    }

    /**
     * 针对key 的field 设置为val
     * @param key
     * @param field
     * @param val
     * @return
     */
    public static Long hset(String key, String field, String val) {
        return jedis.hset(key, field, val);
    }

    /**
     * 当且仅当field不存在时，设置val才会成功
     * @param key
     * @param field
     * @param val
     * @return
     */
    public static Long hsetNx(String key, String field, String val) {
        return jedis.hsetnx(key, field, val);
    }

    /**
     * 返回key中field的个数
     * @param key
     * @return
     */
    public static Long hlen(String key) {
        return jedis.hlen(key);
    }

    /**
     * 返回key 的hash 里的所有field集合
     * @param key
     * @return
     */
    public static Set<String> hkeys(String key) {
        return jedis.hkeys(key);
    }

    /**
     * 返回key hash 里所有的val
     * @param key
     * @return
     */
    public static List<String> hvals(String key) {
        return jedis.hvals(key);
    }

    /**
     * 针对key里的field进行+val操作，跟String 里的incyBy一样的功能
     * @param key
     * @param field
     * @param val
     * @return
     */
    public static Long hIncrBy(String key, String field, Long val) {
        return jedis.hincrBy(key, field, val);
    }

    /**
     * 功能类似与String的incrByFloat
     * @param key
     * @param field
     * @param val
     * @return
     */
    public static Double hIncrByFloat(String key, String field, Double val) {
        return jedis.hincrByFloat(key, field, val);
    }

    /**
     * 将多个filed的结果集返回
     * @param key
     * @param field1
     * @param field2
     * @return
     */
    public static List<String> hMGet(String key, String field1, String field2) {
        return jedis.hmget(key, field1, field2);
    }

    /**
     * 针对多个field - value进行一起设置，成功返回OK
     * @param key
     * @param field1
     * @param val1
     * @param field2
     * @param val2
     * @return
     */
    public static String hMSet(String key, String field1, String val1, String field2, String val2) {
        return jedis.mset(key, field1, val1, field2, val2);
    }


}
