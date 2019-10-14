package cn.choleece.base.framework.redis;

import cn.choleece.base.framework.redis.connection.core.RedisOperations;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

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
 **/
@Configuration
@ConditionalOnClass(RedisOperations.class)
@EnableConfigurationProperties(RedisProperties.class)
@Import({ JedisConnectionConfiguration.class })
public class RedisAutoConfiguration {
}
