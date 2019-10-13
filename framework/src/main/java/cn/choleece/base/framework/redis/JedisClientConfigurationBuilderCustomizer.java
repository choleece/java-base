package cn.choleece.base.framework.redis;

import cn.choleece.base.framework.redis.connection.jedis.JedisClientConfiguration;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-13 23:21
 **/
@FunctionalInterface
public interface JedisClientConfigurationBuilderCustomizer {

    void customize(JedisClientConfiguration.JedisClientConfigurationBuilder clientConfigurationBuilder);
}
