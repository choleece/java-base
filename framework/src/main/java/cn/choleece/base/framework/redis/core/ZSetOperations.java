package cn.choleece.base.framework.redis.core;

import cn.choleece.base.framework.redis.connection.RedisZSetCommands;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.Set;

/**
 * @author choleece
 * @Description: ZSet 操作集合
 * @Date 2019-10-15 22:42
 **/
public interface ZSetOperations<K, V> {
    @Nullable
    Boolean add(K var1, V var2, double var3);

    @Nullable
    Long add(K var1, Set<TypedTuple<V>> var2);

    @Nullable
    Long remove(K var1, Object... var2);

    @Nullable
    Double incrementScore(K var1, V var2, double var3);

    @Nullable
    Long rank(K var1, Object var2);

    @Nullable
    Long reverseRank(K var1, Object var2);

    @Nullable
    Set<V> range(K var1, long var2, long var4);

    @Nullable
    Set<ZSetOperations.TypedTuple<V>> rangeWithScores(K var1, long var2, long var4);

    @Nullable
    Set<V> rangeByScore(K var1, double var2, double var4);

    @Nullable
    Set<ZSetOperations.TypedTuple<V>> rangeByScoreWithScores(K var1, double var2, double var4);

    @Nullable
    Set<V> rangeByScore(K var1, double var2, double var4, long var6, long var8);

    @Nullable
    Set<ZSetOperations.TypedTuple<V>> rangeByScoreWithScores(K var1, double var2, double var4, long var6, long var8);

    @Nullable
    Set<V> reverseRange(K var1, long var2, long var4);

    @Nullable
    Set<ZSetOperations.TypedTuple<V>> reverseRangeWithScores(K var1, long var2, long var4);

    @Nullable
    Set<V> reverseRangeByScore(K var1, double var2, double var4);

    @Nullable
    Set<ZSetOperations.TypedTuple<V>> reverseRangeByScoreWithScores(K var1, double var2, double var4);

    @Nullable
    Set<V> reverseRangeByScore(K var1, double var2, double var4, long var6, long var8);

    @Nullable
    Set<ZSetOperations.TypedTuple<V>> reverseRangeByScoreWithScores(K var1, double var2, double var4, long var6, long var8);

    @Nullable
    Long count(K var1, double var2, double var4);

    @Nullable
    Long size(K var1);

    @Nullable
    Long zCard(K var1);

    @Nullable
    Double score(K var1, Object var2);

    @Nullable
    Long removeRange(K var1, long var2, long var4);

    @Nullable
    Long removeRangeByScore(K var1, double var2, double var4);

    @Nullable
    Long unionAndStore(K var1, K var2, K var3);

    @Nullable
    Long unionAndStore(K var1, Collection<K> var2, K var3);

    @Nullable
    default Long unionAndStore(K key, Collection<K> otherKeys, K destKey, RedisZSetCommands.Aggregate aggregate) {
        return this.unionAndStore(key, otherKeys, destKey, aggregate, RedisZSetCommands.Weights.fromSetCount(1 + otherKeys.size()));
    }

    @Nullable
    Long unionAndStore(K var1, Collection<K> var2, K var3, RedisZSetCommands.Aggregate var4, RedisZSetCommands.Weights var5);

    @Nullable
    Long intersectAndStore(K var1, K var2, K var3);

    @Nullable
    Long intersectAndStore(K var1, Collection<K> var2, K var3);

    @Nullable
    default Long intersectAndStore(K key, Collection<K> otherKeys, K destKey, RedisZSetCommands.Aggregate aggregate) {
        return this.intersectAndStore(key, otherKeys, destKey, aggregate, RedisZSetCommands.Weights.fromSetCount(1 + otherKeys.size()));
    }

    @Nullable
    Long intersectAndStore(K var1, Collection<K> var2, K var3, RedisZSetCommands.Aggregate var4, RedisZSetCommands.Weights var5);

    Cursor<ZSetOperations.TypedTuple<V>> scan(K var1, ScanOptions var2);

    @Nullable
    Set<V> rangeByLex(K var1, RedisZSetCommands.Range var2);

    @Nullable
    Set<V> rangeByLex(K var1, RedisZSetCommands.Range var2, RedisZSetCommands.Limit var3);

    RedisOperations<K, V> getOperations();

    public interface TypedTuple<V> extends Comparable<ZSetOperations.TypedTuple<V>> {
        @Nullable
        V getValue();

        @Nullable
        Double getScore();
    }
}
