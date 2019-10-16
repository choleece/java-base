package cn.choleece.base.framework.redis.serializer;

import org.springframework.lang.Nullable;

/**
 * @author choleece
 * @Description: Redis 序列化接口
 * @Date 2019-10-15 21:31
 **/
public interface RedisSerializer<T> {

    @Nullable
    byte[] serialize(@Nullable T var1) throws SerializationException;

    @Nullable
    T deserialize(@Nullable byte[] var1) throws SerializationException;

    static RedisSerializer<Object> java() {
        return java((ClassLoader)null);
    }

    static RedisSerializer<Object> java(@Nullable ClassLoader classLoader) {
        return new JdkSerializationRedisSerializer(classLoader);
    }

    static RedisSerializer<Object> json() {
        return new GenericJackson2JsonRedisSerializer();
    }

    static RedisSerializer<String> string() {
        return StringRedisSerializer.UTF_8;
    }
}
