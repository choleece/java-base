package cn.choleece.base.framework.redis.core;

import cn.choleece.base.framework.redis.connection.BitFieldSubCommands;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-15 21:48
 **/
public interface ValueOperations <K, V> {

    void set(K key, V value);

    void set(K key, V value, long timeout, TimeUnit unit);

    default void set(K key, V value, Duration timeout) {

        Assert.notNull(timeout, "Timeout must not be null!");

        if (TimeoutUtils.hasMillis(timeout)) {
            set(key, value, timeout.toMillis(), TimeUnit.MILLISECONDS);
        } else {
            set(key, value, timeout.getSeconds(), TimeUnit.SECONDS);
        }
    }

    @Nullable
    Boolean setIfAbsent(K key, V value);

    @Nullable
    Boolean setIfAbsent(K key, V value, long timeout, TimeUnit unit);

    @Nullable
    default Boolean setIfAbsent(K key, V value, Duration timeout) {

        Assert.notNull(timeout, "Timeout must not be null!");

        if (TimeoutUtils.hasMillis(timeout)) {
            return setIfAbsent(key, value, timeout.toMillis(), TimeUnit.MILLISECONDS);
        }

        return setIfAbsent(key, value, timeout.getSeconds(), TimeUnit.SECONDS);
    }

    @Nullable
    Boolean setIfPresent(K key, V value);

    @Nullable
    Boolean setIfPresent(K key, V value, long timeout, TimeUnit unit);

    @Nullable
    default Boolean setIfPresent(K key, V value, Duration timeout) {

        Assert.notNull(timeout, "Timeout must not be null!");

        if (TimeoutUtils.hasMillis(timeout)) {
            return setIfPresent(key, value, timeout.toMillis(), TimeUnit.MILLISECONDS);
        }

        return setIfPresent(key, value, timeout.getSeconds(), TimeUnit.SECONDS);
    }

    void multiSet(Map<? extends K, ? extends V> map);

    @Nullable
    Boolean multiSetIfAbsent(Map<? extends K, ? extends V> map);

    @Nullable
    V get(Object key);

    @Nullable
    V getAndSet(K key, V value);

    @Nullable
    List<V> multiGet(Collection<K> keys);

    @Nullable
    Long increment(K key);

    @Nullable
    Long increment(K key, long delta);

    @Nullable
    Double increment(K key, double delta);

    @Nullable
    Long decrement(K key);

    @Nullable
    Long decrement(K key, long delta);

    @Nullable
    Integer append(K key, String value);

    @Nullable
    String get(K key, long start, long end);

    void set(K key, V value, long offset);

    @Nullable
    Long size(K key);

    @Nullable
    Boolean setBit(K key, long offset, boolean value);

    @Nullable
    Boolean getBit(K key, long offset);

    @Nullable
    List<Long> bitField(K key, BitFieldSubCommands subCommands);

    RedisOperations<K, V> getOperations();
}
