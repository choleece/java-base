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
    void set(K var1, V var2);

    void set(K var1, V var2, long var3, TimeUnit var5);

    default void set(K key, V value, Duration timeout) {
        Assert.notNull(timeout, "Timeout must not be null!");
        if (TimeoutUtils.hasMillis(timeout)) {
            this.set(key, value, timeout.toMillis(), TimeUnit.MILLISECONDS);
        } else {
            this.set(key, value, timeout.getSeconds(), TimeUnit.SECONDS);
        }

    }

    @Nullable
    Boolean setIfAbsent(K var1, V var2);

    @Nullable
    Boolean setIfAbsent(K var1, V var2, long var3, TimeUnit var5);

    @Nullable
    default Boolean setIfAbsent(K key, V value, Duration timeout) {
        Assert.notNull(timeout, "Timeout must not be null!");
        return TimeoutUtils.hasMillis(timeout) ? this.setIfAbsent(key, value, timeout.toMillis(), TimeUnit.MILLISECONDS) : this.setIfAbsent(key, value, timeout.getSeconds(), TimeUnit.SECONDS);
    }

    @Nullable
    Boolean setIfPresent(K var1, V var2);

    @Nullable
    Boolean setIfPresent(K var1, V var2, long var3, TimeUnit var5);

    @Nullable
    default Boolean setIfPresent(K key, V value, Duration timeout) {
        Assert.notNull(timeout, "Timeout must not be null!");
        return TimeoutUtils.hasMillis(timeout) ? this.setIfPresent(key, value, timeout.toMillis(), TimeUnit.MILLISECONDS) : this.setIfPresent(key, value, timeout.getSeconds(), TimeUnit.SECONDS);
    }

    void multiSet(Map<? extends K, ? extends V> var1);

    @Nullable
    Boolean multiSetIfAbsent(Map<? extends K, ? extends V> var1);

    @Nullable
    V get(Object var1);

    @Nullable
    V getAndSet(K var1, V var2);

    @Nullable
    List<V> multiGet(Collection<K> var1);

    @Nullable
    Long increment(K var1);

    @Nullable
    Long increment(K var1, long var2);

    @Nullable
    Double increment(K var1, double var2);

    @Nullable
    Long decrement(K var1);

    @Nullable
    Long decrement(K var1, long var2);

    @Nullable
    Integer append(K var1, String var2);

    @Nullable
    String get(K var1, long var2, long var4);

    void set(K var1, V var2, long var3);

    @Nullable
    Long size(K var1);

    @Nullable
    Boolean setBit(K var1, long var2, boolean var4);

    @Nullable
    Boolean getBit(K var1, long var2);

    @Nullable
    List<Long> bitField(K var1, BitFieldSubCommands var2);

    RedisOperations<K, V> getOperations();
}
