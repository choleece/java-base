package cn.choleece.base.framework.redis.connection.core;

import org.springframework.lang.Nullable;

import java.io.Closeable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author choleece
 * @Description: Redis 操作封装
 * @Date 2019-10-13 21:59
 **/
public interface RedisOperations<K, V> {

    @Nullable
    <T> T execute(RedisCallback<T> var1);

//    @Nullable
//    <T> T execute(SessionCallback<T> var1);
//
//    List<Object> executePipelined(RedisCallback<?> var1);
//
//    List<Object> executePipelined(RedisCallback<?> var1, RedisSerializer<?> var2);
//
//    List<Object> executePipelined(SessionCallback<?> var1);
//
//    List<Object> executePipelined(SessionCallback<?> var1, RedisSerializer<?> var2);
//
//    @Nullable
//    <T> T execute(RedisScript<T> var1, List<K> var2, Object... var3);
//
//    @Nullable
//    <T> T execute(RedisScript<T> var1, RedisSerializer<?> var2, RedisSerializer<T> var3, List<K> var4, Object... var5);
//
//    @Nullable
//    <T extends Closeable> T executeWithStickyConnection(RedisCallback<T> var1);
//
//    @Nullable
//    Boolean hasKey(K var1);
//
//    @Nullable
//    Long countExistingKeys(Collection<K> var1);
//
//    @Nullable
//    Boolean delete(K var1);
//
//    @Nullable
//    Long delete(Collection<K> var1);
//
//    @Nullable
//    Boolean unlink(K var1);
//
//    @Nullable
//    Long unlink(Collection<K> var1);
//
//    @Nullable
//    DataType type(K var1);
//
//    @Nullable
//    Set<K> keys(K var1);
//
//    @Nullable
//    K randomKey();
//
//    void rename(K var1, K var2);
//
//    @Nullable
//    Boolean renameIfAbsent(K var1, K var2);
//
//    @Nullable
//    Boolean expire(K var1, long var2, TimeUnit var4);
//
//    @Nullable
//    Boolean expireAt(K var1, Date var2);
//
//    @Nullable
//    Boolean persist(K var1);
//
//    @Nullable
//    Boolean move(K var1, int var2);
//
//    @Nullable
//    byte[] dump(K var1);
//
//    default void restore(K key, byte[] value, long timeToLive, TimeUnit unit) {
//        this.restore(key, value, timeToLive, unit, false);
//    }
//
//    void restore(K var1, byte[] var2, long var3, TimeUnit var5, boolean var6);
//
//    @Nullable
//    Long getExpire(K var1);
//
//    @Nullable
//    Long getExpire(K var1, TimeUnit var2);
//
//    @Nullable
//    List<V> sort(SortQuery<K> var1);
//
//    @Nullable
//    <T> List<T> sort(SortQuery<K> var1, RedisSerializer<T> var2);
//
//    @Nullable
//    <T> List<T> sort(SortQuery<K> var1, BulkMapper<T, V> var2);
//
//    @Nullable
//    <T, S> List<T> sort(SortQuery<K> var1, BulkMapper<T, S> var2, RedisSerializer<S> var3);
//
//    @Nullable
//    Long sort(SortQuery<K> var1, K var2);
//
//    void watch(K var1);
//
//    void watch(Collection<K> var1);
//
//    void unwatch();
//
//    void multi();
//
//    void discard();
//
//    List<Object> exec();
//
//    List<Object> exec(RedisSerializer<?> var1);
//
//    @Nullable
//    List<RedisClientInfo> getClientList();
//
//    void killClient(String var1, int var2);
//
//    void slaveOf(String var1, int var2);
//
//    void slaveOfNoOne();
//
//    void convertAndSend(String var1, Object var2);
//
//    ClusterOperations<K, V> opsForCluster();
//
//    GeoOperations<K, V> opsForGeo();
//
//    BoundGeoOperations<K, V> boundGeoOps(K var1);
//
//    <HK, HV> HashOperations<K, HK, HV> opsForHash();
//
//    <HK, HV> BoundHashOperations<K, HK, HV> boundHashOps(K var1);
//
//    HyperLogLogOperations<K, V> opsForHyperLogLog();
//
//    ListOperations<K, V> opsForList();
//
//    BoundListOperations<K, V> boundListOps(K var1);
//
//    SetOperations<K, V> opsForSet();
//
//    BoundSetOperations<K, V> boundSetOps(K var1);
//
//    ValueOperations<K, V> opsForValue();
//
//    BoundValueOperations<K, V> boundValueOps(K var1);
//
//    ZSetOperations<K, V> opsForZSet();
//
//    BoundZSetOperations<K, V> boundZSetOps(K var1);
//
//    RedisSerializer<?> getKeySerializer();
//
//    RedisSerializer<?> getValueSerializer();
//
//    RedisSerializer<?> getHashKeySerializer();
//
//    RedisSerializer<?> getHashValueSerializer();
}
