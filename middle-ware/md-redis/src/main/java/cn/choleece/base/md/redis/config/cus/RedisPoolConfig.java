//package cn.choleece.base.md.redis.config.cus;
//
//import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import redis.clients.jedis.Jedis;
//import redis.clients.jedis.JedisPool;
//import redis.clients.jedis.JedisPoolConfig;
//
///**
// * @author choleece
// * @Description: TODO
// * @Date 2020-04-27 07:32
// **/
//@Configuration
//public class RedisPoolConfig {
//
//    @Bean
//    @ConditionalOnBean(value = { JedisPoolProperty.class })
//    public JedisPool initJedisPool(JedisPoolProperty poolProperty) {
//
//        JedisPoolConfig pool = new JedisPoolConfig();
//        pool.setMaxTotal(poolProperty.getMaxTotal());
//        pool.setMaxIdle(poolProperty.getMaxIdle());
//        pool.setMaxWaitMillis(poolProperty.getMaxWaitMills());
//        pool.setTestOnBorrow(poolProperty.getTestOnBorrow());
//        pool.setTestOnReturn(poolProperty.getTestOnReturn());
//
//        return new JedisPool(pool, poolProperty.getHost(), poolProperty.getPort(), poolProperty.getTimeout());
//    }
//
//    @Bean
//    @ConditionalOnBean(value = { JedisPool.class })
//    public Jedis jedis(JedisPool jedisPool) {
//        return jedisPool.getResource();
//    }
//}
