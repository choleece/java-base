package cn.choleece.base.framework.redis;

import cn.choleece.base.framework.redis.connection.RedisConnectionFactory;
import cn.choleece.base.framework.redis.core.RedisOperations;
import cn.choleece.base.framework.redis.serializer.Jackson2JsonRedisSerializer;
import cn.choleece.base.framework.redis.serializer.RedisSerializer;
import cn.choleece.base.framework.redis.serializer.StringRedisSerializer;
import cn.choleece.base.framework.template.CusRedisTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.net.UnknownHostException;

/**
 * @author choleece
 * @Description: Redis 配置自动装配
 * @Date 2019-10-13 23:35
 *
 * EnableConfigurationProperties: 使使用 @ConfigurationProperties 注解的类生效。
 * 参考: https://www.jianshu.com/p/7f54da1cb2eb
 *
 * ConditionalOnClass: 当给定的类名在类路径上存在，则实例化当前Bean
 * 参考: https://www.cnblogs.com/qdhxhz/p/11027546.html(次参考链接包含很多Conditional的注解解释)
 *
 * Import: 提供与 xml 中 <import/> 等效的功能, 允许去导入@Configuration类, ImportSelector 和 ImportBeanDefinitionRegistrar 的具体实现, 以及常规组件类 (这一句划重点)。
 * 类似于 AnnotationConfigApplicationContext.register(java.lang.Class<?>...) 这种操作
 * 参考: https://www.jianshu.com/p/56d4cadbe5c9
 *      https://www.jianshu.com/p/afd2c49394c2
 *
 * 关于自动装配，制作starter:
 * 参考: https://www.jianshu.com/p/bbf439c8a203
 **/
@Configuration
@ConditionalOnClass(RedisOperations.class)
@EnableConfigurationProperties(RedisProperties.class)
@Import({ JedisConnectionConfiguration.class })
public class RedisAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "cusRedisTemplate")
    public CusRedisTemplate<Object, Object> cusRedisTemplate(RedisConnectionFactory redisConnectionFactory)
            throws UnknownHostException {
        CusRedisTemplate<Object, Object> template = new CusRedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        RedisSerializer stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setValueSerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setHashValueSerializer(stringSerializer);
        return template;
    }
}
