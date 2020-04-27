package cn.choleece.base.miaosha.common.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-04-27 07:32
 **/
@Configurable
public class RedisPoolConfig {

    @Bean
    @ConditionalOnBean(value = { JedisPoolProperty.class })
    public JedisPool initJedisPool(JedisPoolProperty poolProperty) {
        JedisPoolConfig pool = new JedisPoolConfig();
        pool.setMaxTotal(poolProperty.getMaxTotal());
        pool.setMaxIdle(poolProperty.getMaxIdel());
        pool.setMaxWaitMillis(poolProperty.getMaxWaitMills());
        pool.setTestOnBorrow(poolProperty.getTestOnBorrow());
        pool.setTestOnReturn(poolProperty.getTestOnReturn());

        return new JedisPool(pool, poolProperty.getHost(), poolProperty.getPort(), poolProperty.getTimeout());
    }

    @Bean
    @ConditionalOnBean(value = { JedisPool.class })
    public Jedis jedis(JedisPool jedisPool) {
        return jedisPool.getResource();
    }
}
