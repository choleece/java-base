package cn.choleece.base.framework.redis.serializer;

import org.springframework.core.CollectionFactory;
import org.springframework.lang.Nullable;

import java.util.*;

/**
 * @author choleece
 * @Description: 序列化工具
 * @Date 2019-10-15 21:34
 **/
public class SerializationUtils {

    static final byte[] EMPTY_ARRAY = new byte[0];

    public SerializationUtils() {
    }

    static boolean isEmpty(@Nullable byte[] data) {
        return data == null || data.length == 0;
    }

    static <T extends Collection<?>> T deserializeValues(@Nullable Collection<byte[]> rawValues, Class<T> type, @Nullable RedisSerializer<?> redisSerializer) {
        if (rawValues == null) {
            return (T) CollectionFactory.createCollection(type, 0);
        } else {
            Collection<Object> values = List.class.isAssignableFrom(type) ? new ArrayList(rawValues.size()) : new LinkedHashSet(rawValues.size());
            Iterator var4 = rawValues.iterator();

            while(var4.hasNext()) {
                byte[] bs = (byte[])var4.next();
                ((Collection)values).add(redisSerializer.deserialize(bs));
            }

            return (T) values;
        }
    }

    public static <T> Set<T> deserialize(@Nullable Set<byte[]> rawValues, @Nullable RedisSerializer<T> redisSerializer) {
        return (Set)deserializeValues(rawValues, Set.class, redisSerializer);
    }

    public static <T> List<T> deserialize(@Nullable List<byte[]> rawValues, @Nullable RedisSerializer<T> redisSerializer) {
        return (List)deserializeValues(rawValues, List.class, redisSerializer);
    }

    public static <T> Collection<T> deserialize(@Nullable Collection<byte[]> rawValues, RedisSerializer<T> redisSerializer) {
        return deserializeValues(rawValues, List.class, redisSerializer);
    }

    public static <T> Map<T, T> deserialize(@Nullable Map<byte[], byte[]> rawValues, RedisSerializer<T> redisSerializer) {
        if (rawValues == null) {
            return Collections.emptyMap();
        } else {
            Map<T, T> ret = new LinkedHashMap(rawValues.size());
            Iterator var3 = rawValues.entrySet().iterator();

            while(var3.hasNext()) {
                Map.Entry<byte[], byte[]> entry = (Map.Entry)var3.next();
                ret.put(redisSerializer.deserialize((byte[])entry.getKey()), redisSerializer.deserialize((byte[])entry.getValue()));
            }

            return ret;
        }
    }

    public static <HK, HV> Map<HK, HV> deserialize(@Nullable Map<byte[], byte[]> rawValues, @Nullable RedisSerializer<HK> hashKeySerializer, @Nullable RedisSerializer<HV> hashValueSerializer) {
        if (rawValues == null) {
            return Collections.emptyMap();
        } else {
            Map<HK, HV> map = new LinkedHashMap(rawValues.size());
            Iterator var4 = rawValues.entrySet().iterator();

            while(var4.hasNext()) {
                Map.Entry<byte[], byte[]> entry = (Map.Entry)var4.next();
                HK key = hashKeySerializer != null ? hashKeySerializer.deserialize((byte[])entry.getKey()) : (HK) entry.getKey();
                HV value = hashValueSerializer != null ? hashValueSerializer.deserialize((byte[])entry.getValue()) : (HV) entry.getValue();
                map.put(key, value);
            }

            return map;
        }
    }
}
