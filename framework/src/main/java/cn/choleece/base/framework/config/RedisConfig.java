package cn.choleece.base.framework.config;

import cn.choleece.base.framework.redis.RedisAutoConfiguration;
import cn.choleece.base.framework.redis.connection.RedisConnectionFactory;
import cn.choleece.base.framework.redis.serializer.Jackson2JsonRedisSerializer;
import cn.choleece.base.framework.redis.serializer.RedisSerializer;
import cn.choleece.base.framework.redis.serializer.StringRedisSerializer;
import cn.choleece.base.framework.template.CusRedisTemplate;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: TODO
 * @author: sf
 * @time: 2019-10-16 17:17
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisConfig {

    @Bean
    public StringRedisSerializer stringRedisSerializer() {
        return new StringRedisSerializer();
    }

    /**
     * 配置自定义redisTemplate
     *
     * @param connectionFactory
     * @return
     */
    @Bean
    public CusRedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        CusRedisTemplate<String, Object> template = new CusRedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setValueSerializer(jackson2JsonRedisSerializer());
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jackson2JsonRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

    /**
     * json序列化
     * @return
     */
    @Bean
    public RedisSerializer<Object> jackson2JsonRedisSerializer() {
        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(mapper);
        return serializer;
    }
}
