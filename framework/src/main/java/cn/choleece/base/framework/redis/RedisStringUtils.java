package cn.choleece.base.framework.redis;

import redis.clients.jedis.Jedis;

/**
 * @author choleece
 * @Description: redis String 常规操作
 * @Date 2020-01-02 22:55
 **/
public class RedisStringUtils {
    static Jedis jedis = RedisConfig.jedis;

    public static void main(String[] args) {
        append("name", "choleece");

        strLength("name");

        getRange("name", 1L, 4L);
    }

    /**
     * 获取key的内容
     * @param key
     * @return
     */
    public static String getString(String key) {
        return jedis.get(key);
    }

    /**
     * 返回append 后value的长度
     * 当key不存在时，此时的append 与set作用类似，当key存在后，append就是追加
     * @param key
     * @param value
     * @return
     */
    public static Long append(String key, String value) {
        Long appendResult = jedis.append(key, value);

        System.out.println("after append: " + getString(key));

        return appendResult;
    }

    /**
     * 返回字符串的长度
     * @param key
     * @return
     */
    public static Long strLength(String key) {
        Long strLen = jedis.strlen(key);

        System.out.println("str len: " + strLen);

        return strLen;
    }

    /**
     * 返回start offset 至 end offset之间的字符串, 从0开始，左、右全闭
     * @param key
     * @param startOffset
     * @param endOffset
     * @return
     */
    public static String getRange(String key, Long startOffset, Long endOffset) {
        String range = jedis.getrange(key, startOffset, endOffset);
        System.out.println(range);

        return range;
    }
}
