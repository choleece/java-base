package cn.choleece.base.framework.redis.poll;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-04-12 16:08
 **/
public class RedisPoolConfig {
    JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();

    private String ip = "localhost";

    private int port = 6379;

    private int timeout = 3000;

    private JedisPool jedisPool = null;

    public void init() {
        jedisPoolConfig.setMaxTotal(1024);
        jedisPoolConfig.setMaxIdle(100);
        jedisPoolConfig.setMaxWaitMillis(10000);
        jedisPoolConfig.setTestOnBorrow(false);
        jedisPoolConfig.setTestOnReturn(true);
        // 初始化JedisPool
        jedisPool = new JedisPool(jedisPoolConfig, ip, port, timeout);
    }

    public RedisPoolConfig() {
        init();
    }

    public Jedis getJedis() {
        return jedisPool.getResource();
    }
}
