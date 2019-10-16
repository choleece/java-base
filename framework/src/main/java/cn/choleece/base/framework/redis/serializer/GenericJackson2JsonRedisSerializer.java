package cn.choleece.base.framework.redis.serializer;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.cache.support.NullValue;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * @author choleece
 * @Description: jackson 序列化
 * @Date 2019-10-15 21:37
 **/
public class GenericJackson2JsonRedisSerializer implements RedisSerializer<Object> {
    private final ObjectMapper mapper;

    public GenericJackson2JsonRedisSerializer() {
        this((String)null);
    }

    public GenericJackson2JsonRedisSerializer(@Nullable String classPropertyTypeName) {
        this(new ObjectMapper());
        this.mapper.registerModule((new SimpleModule()).addSerializer(new GenericJackson2JsonRedisSerializer.NullValueSerializer(classPropertyTypeName)));
        if (StringUtils.hasText(classPropertyTypeName)) {
            this.mapper.enableDefaultTypingAsProperty(ObjectMapper.DefaultTyping.NON_FINAL, classPropertyTypeName);
        } else {
            this.mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        }

    }

    public GenericJackson2JsonRedisSerializer(ObjectMapper mapper) {
        Assert.notNull(mapper, "ObjectMapper must not be null!");
        this.mapper = mapper;
    }

    @Override
    public byte[] serialize(@Nullable Object source) throws SerializationException {
        if (source == null) {
            return SerializationUtils.EMPTY_ARRAY;
        } else {
            try {
                return this.mapper.writeValueAsBytes(source);
            } catch (JsonProcessingException var3) {
                throw new SerializationException("Could not write JSON: " + var3.getMessage(), var3);
            }
        }
    }

    @Override
    public Object deserialize(@Nullable byte[] source) throws SerializationException {
        return this.deserialize(source, Object.class);
    }

    @Nullable
    public <T> T deserialize(@Nullable byte[] source, Class<T> type) throws SerializationException {
        Assert.notNull(type, "Deserialization type must not be null! Pleaes provide Object.class to make use of Jackson2 default typing.");
        if (SerializationUtils.isEmpty(source)) {
            return null;
        } else {
            try {
                return this.mapper.readValue(source, type);
            } catch (Exception var4) {
                throw new SerializationException("Could not read JSON: " + var4.getMessage(), var4);
            }
        }
    }

    private class NullValueSerializer extends StdSerializer<NullValue> {
        private static final long serialVersionUID = 1999052150548658808L;
        private final String classIdentifier;

        NullValueSerializer(@Nullable String classIdentifier) {
            super(NullValue.class);
            this.classIdentifier = StringUtils.hasText(classIdentifier) ? classIdentifier : "@class";
        }

        @Override
        public void serialize(NullValue value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeStartObject();
            jgen.writeStringField(this.classIdentifier, NullValue.class.getName());
            jgen.writeEndObject();
        }
    }
}
