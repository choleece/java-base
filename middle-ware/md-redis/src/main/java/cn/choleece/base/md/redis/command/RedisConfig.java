package cn.choleece.base.md.redis.command;

import redis.clients.jedis.Jedis;

/**
 * @author choleece
 * @Description: Redis 配置
 * @Date 2020-01-02 22:52
 **/
public class RedisConfig {

    private static String host = "localhost";

    private static Integer port = 6379;

    public static final Jedis jedis;

    static {
        jedis = new Jedis(host, port);
    }
}
