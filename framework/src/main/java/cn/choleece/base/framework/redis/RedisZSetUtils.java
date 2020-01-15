package cn.choleece.base.framework.redis;

import redis.clients.jedis.Jedis;

/**
 * @author choleece
 * @Description: ZSet 相关操作
 * @Date 2020-01-08 23:14
 **/
public class RedisZSetUtils {

    static Jedis jedis = RedisConfig.jedis;

    /**
     * 向key里，集合的val不能重复
     * @param key
     * @param score
     * @param val
     * @return
     */
    public static Long zadd(String key, Double score, String val) {
        return jedis.zadd(key, score, val);
    }

    /**
     * 统计key里的val个数
     * @param key
     * @return
     */
    public static Long zcard(String key) {
        return jedis.zcard(key);
    }

    /**
     * 统计min 和 max 之间的个数
     * @param key
     * @param min
     * @param max
     * @return
     */
    public static Long zCountMinAndMax(String key, Double min, Double max) {
        return jedis.zcount(key, min, max);
    }

    /**
     * 在key的val上增加increment 当val不存在时，如同zadd
     * @param key
     * @param increment
     * @param val
     * @return
     */
    public static Double zIncrBy(String key, Double increment, String val) {
        return jedis.zincrby(key, increment, val);
    }


}
