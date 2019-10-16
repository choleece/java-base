package cn.choleece.base.framework.redis.serializer;

import com.sun.xml.internal.ws.encoding.soap.SerializationException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * @author choleece
 * @Description: JDK 序列化
 * @Date 2019-10-15 21:32
 **/
public class JdkSerializationRedisSerializer implements RedisSerializer<Object> {
    private final Converter<Object, byte[]> serializer;
    private final Converter<byte[], Object> deserializer;

    public JdkSerializationRedisSerializer() {
        this(new SerializingConverter(), new DeserializingConverter());
    }

    public JdkSerializationRedisSerializer(@Nullable ClassLoader classLoader) {
        this(new SerializingConverter(), new DeserializingConverter(classLoader));
    }

    public JdkSerializationRedisSerializer(Converter<Object, byte[]> serializer, Converter<byte[], Object> deserializer) {
        Assert.notNull(serializer, "Serializer must not be null!");
        Assert.notNull(deserializer, "Deserializer must not be null!");
        this.serializer = serializer;
        this.deserializer = deserializer;
    }

    @Override
    public Object deserialize(@Nullable byte[] bytes) {
        if (SerializationUtils.isEmpty(bytes)) {
            return null;
        } else {
            try {
                return this.deserializer.convert(bytes);
            } catch (Exception var3) {
                throw new SerializationException("Cannot deserialize", var3);
            }
        }
    }

    @Override
    public byte[] serialize(@Nullable Object object) {
        if (object == null) {
            return SerializationUtils.EMPTY_ARRAY;
        } else {
            try {
                return (byte[])this.serializer.convert(object);
            } catch (Exception var3) {
                throw new SerializationException("Cannot serialize", var3);
            }
        }
    }
}