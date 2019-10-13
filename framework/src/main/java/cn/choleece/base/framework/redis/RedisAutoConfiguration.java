package cn.choleece.base.framework.redis;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author choleece
 * @Description: Redis 配置自动装配
 * @Date 2019-10-13 23:35
 **/
@Configuration
//@ConditionalOnClass(RedisOperations.class)
@EnableConfigurationProperties(RedisProperties.class)
@Import({ JedisConnectionConfiguration.class })
public class RedisAutoConfiguration {
}
