package cn.choleece.base.framework.redis.core;

import cn.choleece.base.framework.exception.DataAccessException;
import cn.choleece.base.framework.redis.connection.BitFieldSubCommands;
import cn.choleece.base.framework.redis.connection.RedisConnection;
import cn.choleece.base.framework.redis.connection.RedisStringCommands;
import cn.choleece.base.framework.redis.core.types.Expiration;
import cn.choleece.base.framework.template.CusRedisTemplate;
import org.springframework.lang.Nullable;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author choleece
 * @Description: 默认的value operations
 * @Date 2019-10-15 22:30
 **/
public class DefaultValueOperations<K, V> extends AbstractOperations<K, V> implements ValueOperations<K, V> {

    DefaultValueOperations(CusRedisTemplate<K, V> template) {
        super(template);
    }

    @Override
    public V get(Object key) {
        return this.execute(new AbstractOperations<K, V>.ValueDeserializingRedisCallback(key) {
            @Override
            protected byte[] inRedis(byte[] rawKey, RedisConnection connection) {
                return connection.get(rawKey);
            }
        }, true);
    }

    @Override
    public V getAndSet(K key, V newValue) {
        final byte[] rawValue = this.rawValue(newValue);
        return this.execute(new AbstractOperations<K, V>.ValueDeserializingRedisCallback(key) {
            @Override
            protected byte[] inRedis(byte[] rawKey, RedisConnection connection) {
                return connection.getSet(rawKey, rawValue);
            }
        }, true);
    }

    @Override
    public Long increment(K key) {
        byte[] rawKey = this.rawKey(key);
        return (Long)this.execute((connection) -> {
            return connection.incr(rawKey);
        }, true);
    }

    @Override
    public Long increment(K key, long delta) {
        byte[] rawKey = this.rawKey(key);
        return (Long)this.execute((connection) -> {
            return connection.incrBy(rawKey, delta);
        }, true);
    }

    @Override
    public Double increment(K key, double delta) {
        byte[] rawKey = this.rawKey(key);
        return (Double)this.execute((connection) -> {
            return connection.incrBy(rawKey, delta);
        }, true);
    }

    @Override
    public Long decrement(K key) {
        byte[] rawKey = this.rawKey(key);
        return (Long)this.execute((connection) -> {
            return connection.decr(rawKey);
        }, true);
    }

    @Override
    public Long decrement(K key, long delta) {
        byte[] rawKey = this.rawKey(key);
        return (Long)this.execute((connection) -> {
            return connection.decrBy(rawKey, delta);
        }, true);
    }

    @Override
    public Integer append(K key, String value) {
        byte[] rawKey = this.rawKey(key);
        byte[] rawString = this.rawString(value);
        return (Integer)this.execute((connection) -> {
            Long result = connection.append(rawKey, rawString);
            return result != null ? result.intValue() : null;
        }, true);
    }

    @Override
    public String get(K key, long start, long end) {
        byte[] rawKey = this.rawKey(key);
        byte[] rawReturn = (byte[])this.execute((connection) -> {
            return connection.getRange(rawKey, start, end);
        }, true);
        return this.deserializeString(rawReturn);
    }

    @Override
    public List<V> multiGet(Collection<K> keys) {
        if (keys.isEmpty()) {
            return Collections.emptyList();
        } else {
            byte[][] rawKeys = new byte[keys.size()][];
            int counter = 0;

            Object hashKey;
            for(Iterator var4 = keys.iterator(); var4.hasNext(); rawKeys[counter++] = this.rawKey(hashKey)) {
                hashKey = var4.next();
            }

            List<byte[]> rawValues = (List)this.execute((connection) -> {
                return connection.mGet(rawKeys);
            }, true);
            return this.deserializeValues(rawValues);
        }
    }

    @Override
    public void multiSet(Map<? extends K, ? extends V> m) {
        if (!m.isEmpty()) {
            Map<byte[], byte[]> rawKeys = new LinkedHashMap(m.size());
            Iterator var3 = m.entrySet().iterator();

            while(var3.hasNext()) {
                Map.Entry<? extends K, ? extends V> entry = (Map.Entry)var3.next();
                rawKeys.put(this.rawKey(entry.getKey()), this.rawValue(entry.getValue()));
            }

            this.execute((connection) -> {
                connection.mSet(rawKeys);
                return null;
            }, true);
        }
    }

    @Override
    public Boolean multiSetIfAbsent(Map<? extends K, ? extends V> m) {
        if (m.isEmpty()) {
            return true;
        } else {
            Map<byte[], byte[]> rawKeys = new LinkedHashMap(m.size());
            Iterator var3 = m.entrySet().iterator();

            while(var3.hasNext()) {
                Map.Entry<? extends K, ? extends V> entry = (Map.Entry)var3.next();
                rawKeys.put(this.rawKey(entry.getKey()), this.rawValue(entry.getValue()));
            }

            return (Boolean)this.execute((connection) -> {
                return connection.mSetNX(rawKeys);
            }, true);
        }
    }

    @Override
    public void set(K key, V value) {
        final byte[] rawValue = this.rawValue(value);
        this.execute(new AbstractOperations<K, V>.ValueDeserializingRedisCallback(key) {
            @Override
            protected byte[] inRedis(byte[] rawKey, RedisConnection connection) {
                connection.set(rawKey, rawValue);
                return null;
            }
        }, true);
    }

    @Override
    public void set(K key, V value, final long timeout, final TimeUnit unit) {
        final byte[] rawKey = this.rawKey(key);
        final byte[] rawValue = this.rawValue(value);
        this.execute(new RedisCallback<Object>() {

            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                this.potentiallyUsePsetEx(connection);
                return null;
            }

            public void potentiallyUsePsetEx(RedisConnection connection) {
                if (!TimeUnit.MILLISECONDS.equals(unit) || !this.failsafeInvokePsetEx(connection)) {
                    connection.setEx(rawKey, TimeoutUtils.toSeconds(timeout, unit), rawValue);
                }

            }

            private boolean failsafeInvokePsetEx(RedisConnection connection) {
                boolean failed = false;

                try {
                    connection.pSetEx(rawKey, timeout, rawValue);
                } catch (UnsupportedOperationException var4) {
                    failed = true;
                }

                return !failed;
            }
        }, true);
    }

    @Override
    public Boolean setIfAbsent(K key, V value) {
        byte[] rawKey = this.rawKey(key);
        byte[] rawValue = this.rawValue(value);
        return (Boolean)this.execute((connection) -> {
            return connection.setNX(rawKey, rawValue);
        }, true);
    }

    @Override
    public Boolean setIfAbsent(K key, V value, long timeout, TimeUnit unit) {
        byte[] rawKey = this.rawKey(key);
        byte[] rawValue = this.rawValue(value);
        Expiration expiration = Expiration.from(timeout, unit);
        return (Boolean)this.execute((connection) -> {
            return connection.set(rawKey, rawValue, expiration, RedisStringCommands.SetOption.ifAbsent());
        }, true);
    }

    @Override
    @Nullable
    public Boolean setIfPresent(K key, V value) {
        byte[] rawKey = this.rawKey(key);
        byte[] rawValue = this.rawValue(value);
        return (Boolean)this.execute((connection) -> {
            return connection.set(rawKey, rawValue, Expiration.persistent(), RedisStringCommands.SetOption.ifPresent());
        }, true);
    }

    @Override
    @Nullable
    public Boolean setIfPresent(K key, V value, long timeout, TimeUnit unit) {
        byte[] rawKey = this.rawKey(key);
        byte[] rawValue = this.rawValue(value);
        Expiration expiration = Expiration.from(timeout, unit);
        return (Boolean)this.execute((connection) -> {
            return connection.set(rawKey, rawValue, expiration, RedisStringCommands.SetOption.ifPresent());
        }, true);
    }

    @Override
    public void set(K key, V value, long offset) {
        byte[] rawKey = this.rawKey(key);
        byte[] rawValue = this.rawValue(value);
        this.execute((connection) -> {
            connection.setRange(rawKey, rawValue, offset);
            return null;
        }, true);
    }

    @Override
    public Long size(K key) {
        byte[] rawKey = this.rawKey(key);
        return (Long)this.execute((connection) -> {
            return connection.strLen(rawKey);
        }, true);
    }

    @Override
    public Boolean setBit(K key, long offset, boolean value) {
        byte[] rawKey = this.rawKey(key);
        return (Boolean)this.execute((connection) -> {
            return connection.setBit(rawKey, offset, value);
        }, true);
    }

    @Override
    public Boolean getBit(K key, long offset) {
        byte[] rawKey = this.rawKey(key);
        return (Boolean)this.execute((connection) -> {
            return connection.getBit(rawKey, offset);
        }, true);
    }

    @Override
    public List<Long> bitField(K key, BitFieldSubCommands subCommands) {
        byte[] rawKey = this.rawKey(key);
        return (List)this.execute((connection) -> {
            return connection.bitField(rawKey, subCommands);
        }, true);
    }
}
