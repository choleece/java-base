package cn.choleece.base.framework.redis.serializer;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author choleece
 * @Description: 字符串序列化
 * @Date 2019-10-15 21:43
 **/
public class StringRedisSerializer implements RedisSerializer<String> {
    private final Charset charset;
    public static final StringRedisSerializer US_ASCII;
    public static final StringRedisSerializer ISO_8859_1;
    public static final StringRedisSerializer UTF_8;

    public StringRedisSerializer() {
        this(StandardCharsets.UTF_8);
    }

    public StringRedisSerializer(Charset charset) {
        Assert.notNull(charset, "Charset must not be null!");
        this.charset = charset;
    }

    @Override
    public String deserialize(@Nullable byte[] bytes) {
        return bytes == null ? null : new String(bytes, this.charset);
    }

    @Override
    public byte[] serialize(@Nullable String string) {
        return string == null ? null : string.getBytes(this.charset);
    }

    static {
        US_ASCII = new StringRedisSerializer(StandardCharsets.US_ASCII);
        ISO_8859_1 = new StringRedisSerializer(StandardCharsets.ISO_8859_1);
        UTF_8 = new StringRedisSerializer(StandardCharsets.UTF_8);
    }
}

