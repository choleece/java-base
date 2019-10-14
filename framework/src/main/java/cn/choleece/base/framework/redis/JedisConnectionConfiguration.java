package cn.choleece.base.framework.redis;

import cn.choleece.base.framework.redis.connection.RedisClusterConfiguration;
import cn.choleece.base.framework.redis.connection.RedisSentinelConfiguration;
import org.springframework.beans.factory.ObjectProvider;

/**
 * @author choleece
 * @Description: Jedis 客户端配置
 * @Date 2019-10-13 23:22
 **/
public class JedisConnectionConfiguration extends RedisConnectionConfiguration {

    private final RedisProperties properties;

    private final ObjectProvider<JedisClientConfigurationBuilderCustomizer> builderCustomizers;

    JedisConnectionConfiguration(RedisProperties properties,
                                 ObjectProvider<RedisSentinelConfiguration> sentinelConfiguration,
                                 ObjectProvider<RedisClusterConfiguration> clusterConfiguration,
                                 ObjectProvider<JedisClientConfigurationBuilderCustomizer> builderCustomizers) {
        super(properties, sentinelConfiguration, clusterConfiguration);
        this.properties = properties;
        this.builderCustomizers = builderCustomizers;
    }
}
