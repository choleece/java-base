package cn.choleece.base.framework.redis.core;

import cn.choleece.base.framework.redis.connection.DefaultTuple;
import cn.choleece.base.framework.redis.connection.RedisConnection;
import cn.choleece.base.framework.redis.connection.RedisZSetCommands;
import cn.choleece.base.framework.redis.serializer.RedisSerializer;
import cn.choleece.base.framework.redis.serializer.SerializationUtils;
import cn.choleece.base.framework.template.CusRedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-15 22:31
 **/
public class AbstractOperations<K, V> {

    CusRedisTemplate<K, V> template;

    AbstractOperations(CusRedisTemplate<K, V> template) {
        this.template = template;
    }

    RedisSerializer keySerializer() {
        return this.template.getKeySerializer();
    }

    RedisSerializer valueSerializer() {
        return this.template.getValueSerializer();
    }

    RedisSerializer hashKeySerializer() {
        return this.template.getHashKeySerializer();
    }

    RedisSerializer hashValueSerializer() {
        return this.template.getHashValueSerializer();
    }

    RedisSerializer stringSerializer() {
        return this.template.getStringSerializer();
    }

    @Nullable
    <T> T execute(RedisCallback<T> callback, boolean b) {
        return this.template.execute(callback, b);
    }

    public RedisOperations<K, V> getOperations() {
        return this.template;
    }

    byte[] rawKey(Object key) {
        Assert.notNull(key, "non null key required");
        return this.keySerializer() == null && key instanceof byte[] ? (byte[])((byte[])key) : this.keySerializer().serialize(key);
    }

    byte[] rawString(String key) {
        return this.stringSerializer().serialize(key);
    }

    byte[] rawValue(Object value) {
        return this.valueSerializer() == null && value instanceof byte[] ? (byte[])((byte[])value) : this.valueSerializer().serialize(value);
    }

    byte[][] rawValues(Object... values) {
        byte[][] rawValues = new byte[values.length][];
        int i = 0;
        Object[] var4 = values;
        int var5 = values.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Object value = var4[var6];
            rawValues[i++] = this.rawValue(value);
        }

        return rawValues;
    }

    byte[][] rawValues(Collection<V> values) {
        Assert.notEmpty(values, "Values must not be 'null' or empty.");
        Assert.noNullElements(values.toArray(), "Values must not contain 'null' value.");
        byte[][] rawValues = new byte[values.size()][];
        int i = 0;

        Object value;
        for(Iterator var4 = values.iterator(); var4.hasNext(); rawValues[i++] = this.rawValue(value)) {
            value = var4.next();
        }

        return rawValues;
    }

    <HK> byte[] rawHashKey(HK hashKey) {
        Assert.notNull(hashKey, "non null hash key required");
        return this.hashKeySerializer() == null && hashKey instanceof byte[] ? (byte[])((byte[])hashKey) : this.hashKeySerializer().serialize(hashKey);
    }

    <HK> byte[][] rawHashKeys(HK... hashKeys) {
        byte[][] rawHashKeys = new byte[hashKeys.length][];
        int i = 0;
        Object[] var4 = hashKeys;
        int var5 = hashKeys.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            HK hashKey = (HK) var4[var6];
            rawHashKeys[i++] = this.rawHashKey(hashKey);
        }

        return rawHashKeys;
    }

    <HV> byte[] rawHashValue(HV value) {
        return this.hashValueSerializer() == null && value instanceof byte[] ? (byte[])((byte[])value) : this.hashValueSerializer().serialize(value);
    }

    byte[][] rawKeys(K key, K otherKey) {
        byte[][] rawKeys = new byte[][]{this.rawKey(key), this.rawKey(key)};
        return rawKeys;
    }

    byte[][] rawKeys(Collection<K> keys) {
        return this.rawKeys((K)null, (Collection)keys);
    }

    byte[][] rawKeys(K key, Collection<K> keys) {
        byte[][] rawKeys = new byte[keys.size() + (key != null ? 1 : 0)][];
        int i = 0;
        if (key != null) {
            rawKeys[i++] = this.rawKey(key);
        }

        Object k;
        for(Iterator var5 = keys.iterator(); var5.hasNext(); rawKeys[i++] = this.rawKey(k)) {
            k = var5.next();
        }

        return rawKeys;
    }

    Set<V> deserializeValues(Set<byte[]> rawValues) {
        return this.valueSerializer() == null ? (Set<V>) rawValues : SerializationUtils.deserialize(rawValues, this.valueSerializer());
    }

    Set<ZSetOperations.TypedTuple<V>> deserializeTupleValues(Collection<RedisZSetCommands.Tuple> rawValues) {
        if (rawValues == null) {
            return null;
        } else {
            Set<ZSetOperations.TypedTuple<V>> set = new LinkedHashSet(rawValues.size());
            Iterator var3 = rawValues.iterator();

            while(var3.hasNext()) {
                RedisZSetCommands.Tuple rawValue = (RedisZSetCommands.Tuple)var3.next();
                set.add(this.deserializeTuple(rawValue));
            }

            return set;
        }
    }

    ZSetOperations.TypedTuple<V> deserializeTuple(RedisZSetCommands.Tuple tuple) {
        Object value = tuple.getValue();
        if (this.valueSerializer() != null) {
            value = this.valueSerializer().deserialize(tuple.getValue());
        }

        return new DefaultTypedTuple(value, tuple.getScore());
    }

    Set<RedisZSetCommands.Tuple> rawTupleValues(Set<ZSetOperations.TypedTuple<V>> values) {
        if (values == null) {
            return null;
        } else {
            Set<RedisZSetCommands.Tuple> rawTuples = new LinkedHashSet(values.size());

            ZSetOperations.TypedTuple value;
            byte[] rawValue;
            for(Iterator var3 = values.iterator(); var3.hasNext(); rawTuples.add(new DefaultTuple(rawValue, value.getScore()))) {
                value = (ZSetOperations.TypedTuple)var3.next();
                if (this.valueSerializer() == null && value.getValue() instanceof byte[]) {
                    rawValue = (byte[])((byte[])value.getValue());
                } else {
                    rawValue = this.valueSerializer().serialize(value.getValue());
                }
            }

            return rawTuples;
        }
    }

    List<V> deserializeValues(List<byte[]> rawValues) {
        return this.valueSerializer() == null ? (List<V>)rawValues : SerializationUtils.deserialize(rawValues, this.valueSerializer());
    }

    <T> Set<T> deserializeHashKeys(Set<byte[]> rawKeys) {
        return this.hashKeySerializer() == null ? (Set<T>)rawKeys : SerializationUtils.deserialize(rawKeys, this.hashKeySerializer());
    }

    <T> List<T> deserializeHashValues(List<byte[]> rawValues) {
        return this.hashValueSerializer() == null ? (List<T>)rawValues : SerializationUtils.deserialize(rawValues, this.hashValueSerializer());
    }

    <HK, HV> Map<HK, HV> deserializeHashMap(@Nullable Map<byte[], byte[]> entries) {
        if (entries == null) {
            return null;
        } else {
            Map<HK, HV> map = new LinkedHashMap(entries.size());
            Iterator var3 = entries.entrySet().iterator();

            while(var3.hasNext()) {
                Map.Entry<byte[], byte[]> entry = (Map.Entry)var3.next();
                map.put(this.deserializeHashKey((byte[])entry.getKey()), this.deserializeHashValue((byte[])entry.getValue()));
            }

            return map;
        }
    }

    K deserializeKey(byte[] value) {
        return (K) (this.keySerializer() == null ? value : this.keySerializer().deserialize(value));
    }

    Set<K> deserializeKeys(Set<byte[]> keys) {
        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptySet();
        } else {
            Set<K> result = new LinkedHashSet(keys.size());
            Iterator var3 = keys.iterator();

            while(var3.hasNext()) {
                byte[] key = (byte[])var3.next();
                result.add(this.deserializeKey(key));
            }

            return result;
        }
    }

    V deserializeValue(byte[] value) {
        return (V) (this.valueSerializer() == null ? value : this.valueSerializer().deserialize(value));
    }

    String deserializeString(byte[] value) {
        return (String)this.stringSerializer().deserialize(value);
    }

    <HK> HK deserializeHashKey(byte[] value) {
        return (HK) (this.hashKeySerializer() == null ? value : this.hashKeySerializer().deserialize(value));
    }

    <HV> HV deserializeHashValue(byte[] value) {
        return (HV) (this.hashValueSerializer() == null ? value : this.hashValueSerializer().deserialize(value));
    }

//    GeoResults<GeoLocation<V>> deserializeGeoResults(GeoResults<GeoLocation<byte[]>> source) {
//        return this.valueSerializer() == null ? (GeoResults)source : (GeoResults)Converters.deserializingGeoResultsConverter(this.valueSerializer()).convert(source);
//    }

    abstract class ValueDeserializingRedisCallback implements RedisCallback<V> {
        private Object key;

        public ValueDeserializingRedisCallback(Object key) {
            this.key = key;
        }

        @Override
        public final V doInRedis(RedisConnection connection) {
            byte[] result = this.inRedis(AbstractOperations.this.rawKey(this.key), connection);
            return AbstractOperations.this.deserializeValue(result);
        }

        @Nullable
        protected abstract byte[] inRedis(byte[] var1, RedisConnection var2);
    }
}
