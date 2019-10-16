package cn.choleece.base.framework.redis.connection;

import cn.choleece.base.framework.redis.core.Cursor;
import cn.choleece.base.framework.redis.core.ScanOptions;
import cn.choleece.base.framework.redis.core.types.Expiration;
import cn.choleece.base.framework.redis.core.types.RedisClientInfo;
import org.springframework.lang.Nullable;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @description: TODO
 * @author: sf
 * @time: 2019-10-14 15:30
 */
public interface DefaultedRedisConnection extends RedisConnection {

    @Override
    @Deprecated
    default Boolean exists(byte[] key) {
        return keyCommands().exists(key);
    }

    /** @deprecated in favor of {@link RedisConnection#keyCommands()}. */
    @Override
    @Deprecated
    default Long exists(byte[]... keys) {
        return keyCommands().exists(keys);
    }

    /** @deprecated in favor of {@link RedisConnection#keyCommands()}. */
    @Override
    @Deprecated
    default Long del(byte[]... keys) {
        return keyCommands().del(keys);
    }

    /** @deprecated in favor of {@link RedisConnection#keyCommands()}. */
    @Override
    @Deprecated
    default Long unlink(byte[]... keys) {
        return keyCommands().unlink(keys);
    }

    /** @deprecated in favor of {@link RedisConnection#keyCommands()}. */
    @Override
    @Deprecated
    default DataType type(byte[] pattern) {
        return keyCommands().type(pattern);
    }

    /** @deprecated in favor of {@link RedisConnection#keyCommands()}. */
    @Override
    @Deprecated
    default Long touch(byte[]... keys) {
        return keyCommands().touch(keys);
    }

    /** @deprecated in favor of {@link RedisConnection#keyCommands()}. */
    @Override
    @Deprecated
    default Set<byte[]> keys(byte[] pattern) {
        return keyCommands().keys(pattern);
    }

    /** @deprecated in favor of {@link RedisConnection#keyCommands()}. */
    @Override
    @Deprecated
    default Cursor<byte[]> scan(ScanOptions options) {
        return keyCommands().scan(options);
    }

    /** @deprecated in favor of {@link RedisConnection#keyCommands()}. */
    @Override
    @Deprecated
    default byte[] randomKey() {
        return keyCommands().randomKey();
    }

    /** @deprecated in favor of {@link RedisConnection#keyCommands()}. */
    @Override
    @Deprecated
    default void rename(byte[] sourceKey, byte[] targetKey) {
        keyCommands().rename(sourceKey, targetKey);
    }

    /** @deprecated in favor of {@link RedisConnection#keyCommands()}. */
    @Override
    @Deprecated
    default Boolean renameNX(byte[] sourceKey, byte[] targetKey) {
        return keyCommands().renameNX(sourceKey, targetKey);
    }

    /** @deprecated in favor of {@link RedisConnection#keyCommands()}. */
    @Override
    @Deprecated
    default Boolean expire(byte[] key, long seconds) {
        return keyCommands().expire(key, seconds);
    }

    /** @deprecated in favor of {@link RedisConnection#keyCommands()}. */
    @Override
    @Deprecated
    default Boolean persist(byte[] key) {
        return keyCommands().persist(key);
    }

    /** @deprecated in favor of {@link RedisConnection#keyCommands()}. */
    @Override
    @Deprecated
    default Boolean move(byte[] key, int dbIndex) {
        return keyCommands().move(key, dbIndex);
    }

    /** @deprecated in favor of {@link RedisConnection#keyCommands()}. */
    @Override
    @Deprecated
    default void restore(byte[] key, long ttlInMillis, byte[] serializedValue, boolean replace) {
        keyCommands().restore(key, ttlInMillis, serializedValue, replace);
    }

    /** @deprecated in favor of {@link RedisConnection#keyCommands()}. */
    @Override
    @Deprecated
    default Long pTtl(byte[] key) {
        return keyCommands().pTtl(key);
    }

    /** @deprecated in favor of {@link RedisConnection#keyCommands()}. */
    @Override
    @Deprecated
    default Long pTtl(byte[] key, TimeUnit timeUnit) {
        return keyCommands().pTtl(key, timeUnit);
    }

    /** @deprecated in favor of {@link RedisConnection#keyCommands()}. */
    @Override
    @Deprecated
    default Boolean pExpire(byte[] key, long millis) {
        return keyCommands().pExpire(key, millis);
    }

    /** @deprecated in favor of {@link RedisConnection#keyCommands()}. */
    @Override
    @Deprecated
    default Boolean pExpireAt(byte[] key, long unixTimeInMillis) {
        return keyCommands().pExpireAt(key, unixTimeInMillis);
    }

    /** @deprecated in favor of {@link RedisConnection#keyCommands()}. */
    @Override
    @Deprecated
    default Boolean expireAt(byte[] key, long unixTime) {
        return keyCommands().expireAt(key, unixTime);
    }

    /** @deprecated in favor of {@link RedisConnection#keyCommands()}. */
    @Override
    @Deprecated
    default Long ttl(byte[] key) {
        return keyCommands().ttl(key);
    }

    /** @deprecated in favor of {@link RedisConnection#keyCommands()}. */
    @Override
    @Deprecated
    default Long ttl(byte[] key, TimeUnit timeUnit) {
        return keyCommands().ttl(key, timeUnit);
    }

    /** @deprecated in favor of {@link RedisConnection#keyCommands()}. */
    @Override
    @Deprecated
    default byte[] dump(byte[] key) {
        return keyCommands().dump(key);
    }

    /** @deprecated in favor of {@link RedisConnection#keyCommands()}. */
    @Override
    @Deprecated
    default List<byte[]> sort(byte[] key, SortParameters params) {
        return keyCommands().sort(key, params);
    }

    /** @deprecated in favor of {@link RedisConnection#keyCommands()}. */
    @Override
    @Deprecated
    default Long sort(byte[] key, SortParameters params, byte[] sortKey) {
        return keyCommands().sort(key, params, sortKey);
    }

    /** @deprecated in favor of {@link RedisConnection#keyCommands()}. */
    @Override
    @Deprecated
    default ValueEncoding encodingOf(byte[] key) {
        return keyCommands().encodingOf(key);
    }

    /** @deprecated in favor of {@link RedisConnection#keyCommands()}. */
    @Override
    @Deprecated
    default Duration idletime(byte[] key) {
        return keyCommands().idletime(key);
    }

    /** @deprecated in favor of {@link RedisConnection#keyCommands()}. */
    @Override
    @Deprecated
    default Long refcount(byte[] key) {
        return keyCommands().refcount(key);
    }

    // STRING COMMANDS

    /** @deprecated in favor of {@link RedisConnection#stringCommands()}}. */
    @Override
    @Deprecated
    default byte[] get(byte[] key) {
        return stringCommands().get(key);
    }

    /** @deprecated in favor of {@link RedisConnection#stringCommands()}}. */
    @Override
    @Deprecated
    default byte[] getSet(byte[] key, byte[] value) {
        return stringCommands().getSet(key, value);
    }

    /** @deprecated in favor of {@link RedisConnection#stringCommands()}}. */
    @Override
    @Deprecated
    default List<byte[]> mGet(byte[]... keys) {
        return stringCommands().mGet(keys);
    }

    /** @deprecated in favor of {@link RedisConnection#stringCommands()}}. */
    @Override
    @Deprecated
    default Boolean set(byte[] key, byte[] value) {
        return stringCommands().set(key, value);
    }

    /** @deprecated in favor of {@link RedisConnection#stringCommands()}}. */
    @Override
    @Deprecated
    default Boolean set(byte[] key, byte[] value, Expiration expiration, SetOption option) {
        return stringCommands().set(key, value, expiration, option);
    }

    /** @deprecated in favor of {@link RedisConnection#stringCommands()}}. */
    @Override
    @Deprecated
    default Boolean setNX(byte[] key, byte[] value) {
        return stringCommands().setNX(key, value);
    }

    /** @deprecated in favor of {@link RedisConnection#stringCommands()}}. */
    @Override
    @Deprecated
    default Boolean setEx(byte[] key, long seconds, byte[] value) {
        return stringCommands().setEx(key, seconds, value);
    }

    /** @deprecated in favor of {@link RedisConnection#stringCommands()}}. */
    @Override
    @Deprecated
    default Boolean pSetEx(byte[] key, long milliseconds, byte[] value) {
        return stringCommands().pSetEx(key, milliseconds, value);
    }

    /** @deprecated in favor of {@link RedisConnection#stringCommands()}}. */
    @Override
    @Deprecated
    default Boolean mSet(Map<byte[], byte[]> tuple) {
        return stringCommands().mSet(tuple);
    }

    /** @deprecated in favor of {@link RedisConnection#stringCommands()}}. */
    @Override
    @Deprecated
    default Boolean mSetNX(Map<byte[], byte[]> tuple) {
        return stringCommands().mSetNX(tuple);
    }

    /** @deprecated in favor of {@link RedisConnection#stringCommands()}}. */
    @Override
    @Deprecated
    default Long incr(byte[] key) {
        return stringCommands().incr(key);
    }

    /** @deprecated in favor of {@link RedisConnection#stringCommands()}}. */
    @Override
    @Deprecated
    default Double incrBy(byte[] key, double value) {
        return stringCommands().incrBy(key, value);
    }

    /** @deprecated in favor of {@link RedisConnection#stringCommands()}}. */
    @Override
    @Deprecated
    default Long incrBy(byte[] key, long value) {
        return stringCommands().incrBy(key, value);
    }

    /** @deprecated in favor of {@link RedisConnection#stringCommands()}}. */
    @Override
    @Deprecated
    default Long decr(byte[] key) {
        return stringCommands().decr(key);
    }

    /** @deprecated in favor of {@link RedisConnection#stringCommands()}}. */
    @Override
    @Deprecated
    default Long decrBy(byte[] key, long value) {
        return stringCommands().decrBy(key, value);
    }

    /** @deprecated in favor of {@link RedisConnection#stringCommands()}}. */
    @Override
    @Deprecated
    default Long append(byte[] key, byte[] value) {
        return stringCommands().append(key, value);
    }

    /** @deprecated in favor of {@link RedisConnection#stringCommands()}}. */
    @Override
    @Deprecated
    default byte[] getRange(byte[] key, long start, long end) {
        return stringCommands().getRange(key, start, end);
    }

    /** @deprecated in favor of {@link RedisConnection#stringCommands()}}. */
    @Override
    @Deprecated
    default void setRange(byte[] key, byte[] value, long offset) {
        stringCommands().setRange(key, value, offset);
    }

    /** @deprecated in favor of {@link RedisConnection#stringCommands()}}. */
    @Override
    @Deprecated
    default Boolean getBit(byte[] key, long offset) {
        return stringCommands().getBit(key, offset);
    }

    /** @deprecated in favor of {@link RedisConnection#stringCommands()}}. */
    @Override
    @Deprecated
    default Boolean setBit(byte[] key, long offset, boolean value) {
        return stringCommands().setBit(key, offset, value);
    }

    /** @deprecated in favor of {@link RedisConnection#stringCommands()}}. */
    @Override
    @Deprecated
    default Long bitCount(byte[] key) {
        return stringCommands().bitCount(key);
    }

    /** @deprecated in favor of {@link RedisConnection#stringCommands()}}. */
    @Override
    @Deprecated
    default Long bitCount(byte[] key, long start, long end) {
        return stringCommands().bitCount(key, start, end);
    }

    /** @deprecated in favor of {@link RedisConnection#stringCommands()}}. */
    @Override
    @Deprecated
    default List<Long> bitField(byte[] key, BitFieldSubCommands subCommands) {
        return stringCommands().bitField(key, subCommands);
    }

    /** @deprecated in favor of {@link RedisConnection#stringCommands()}}. */
    @Override
    @Deprecated
    default Long bitOp(BitOperation op, byte[] destination, byte[]... keys) {
        return stringCommands().bitOp(op, destination, keys);
    }

    /** @deprecated in favor of {@link RedisConnection#stringCommands()}}. */
    @Override
    @Deprecated
    default Long bitPos(byte[] key, boolean bit, cn.choleece.base.framework.data.domain.Range<Long> range) {
        return stringCommands().bitPos(key, bit, range);
    }

    /** @deprecated in favor of {@link RedisConnection#stringCommands()}}. */
    @Override
    @Deprecated
    default Long strLen(byte[] key) {
        return stringCommands().strLen(key);
    }

    // SERVER COMMANDS

    /** @deprecated in favor of {@link RedisConnection#serverCommands()}. */
    @Override
    @Deprecated
    default void bgWriteAof() {
        serverCommands().bgWriteAof();
    }

    /** @deprecated in favor of {@link RedisConnection#serverCommands()}. */
    @Override
    @Deprecated
    default void bgReWriteAof() {
        serverCommands().bgReWriteAof();
    }

    /** @deprecated in favor of {@link RedisConnection#serverCommands()}. */
    @Override
    @Deprecated
    default void bgSave() {
        serverCommands().bgSave();
    }

    /** @deprecated in favor of {@link RedisConnection#serverCommands()}. */
    @Override
    @Deprecated
    default Long lastSave() {
        return serverCommands().lastSave();
    }

    /** @deprecated in favor of {@link RedisConnection#serverCommands()}. */
    @Override
    @Deprecated
    default void save() {
        serverCommands().save();
    }

    /** @deprecated in favor of {@link RedisConnection#serverCommands()}. */
    @Override
    @Deprecated
    default Long dbSize() {
        return serverCommands().dbSize();
    }

    /** @deprecated in favor of {@link RedisConnection#serverCommands()}. */
    @Override
    @Deprecated
    default void flushDb() {
        serverCommands().flushDb();
    }

    /** @deprecated in favor of {@link RedisConnection#serverCommands()}. */
    @Override
    @Deprecated
    default void flushAll() {
        serverCommands().flushAll();
    }

    /** @deprecated in favor of {@link RedisConnection#serverCommands()}. */
    @Override
    @Deprecated
    default Properties info() {
        return serverCommands().info();
    }

    /** @deprecated in favor of {@link RedisConnection#serverCommands()}. */
    @Override
    @Deprecated
    default Properties info(String section) {
        return serverCommands().info(section);
    }

    /** @deprecated in favor of {@link RedisConnection#serverCommands()}. */
    @Override
    @Deprecated
    default void shutdown() {
        serverCommands().shutdown();
    }

    /** @deprecated in favor of {@link RedisConnection#serverCommands()}. */
    @Override
    @Deprecated
    default void shutdown(ShutdownOption option) {
        serverCommands().shutdown(option);
    }

    /** @deprecated in favor of {@link RedisConnection#serverCommands()}. */
    @Override
    @Deprecated
    default Properties getConfig(String pattern) {
        return serverCommands().getConfig(pattern);
    }

    /** @deprecated in favor of {@link RedisConnection#serverCommands()}. */
    @Override
    @Deprecated
    default void setConfig(String param, String value) {
        serverCommands().setConfig(param, value);
    }

    /** @deprecated in favor of {@link RedisConnection#serverCommands()}. */
    @Override
    @Deprecated
    default void resetConfigStats() {
        serverCommands().resetConfigStats();
    }

    /** @deprecated in favor of {@link RedisConnection#serverCommands()}. */
    @Override
    @Deprecated
    default Long time() {
        return serverCommands().time();
    }

    /** @deprecated in favor of {@link RedisConnection#serverCommands()}. */
    @Override
    @Deprecated
    default void killClient(String host, int port) {
        serverCommands().killClient(host, port);
    }

    /** @deprecated in favor of {@link RedisConnection#serverCommands()}. */
    @Override
    @Deprecated
    default void setClientName(byte[] name) {
        serverCommands().setClientName(name);
    }

    /** @deprecated in favor of {@link RedisConnection#serverCommands()}. */
    @Override
    @Deprecated
    default String getClientName() {
        return serverCommands().getClientName();
    }

    /** @deprecated in favor of {@link RedisConnection#serverCommands()}. */
    @Override
    @Deprecated
    default List<RedisClientInfo> getClientList() {
        return serverCommands().getClientList();
    }

    /** @deprecated in favor of {@link RedisConnection#serverCommands()}. */
    @Override
    @Deprecated
    default void slaveOf(String host, int port) {
        serverCommands().slaveOf(host, port);
    }

    /** @deprecated in favor of {@link RedisConnection#serverCommands()}. */
    @Override
    @Deprecated
    default void slaveOfNoOne() {
        serverCommands().slaveOfNoOne();
    }

    /** @deprecated in favor of {@link RedisConnection#serverCommands()}. */
    @Override
    @Deprecated
    default void migrate(byte[] key, RedisNode target, int dbIndex, @Nullable MigrateOption option) {
        serverCommands().migrate(key, target, dbIndex, option);
    }

    /** @deprecated in favor of {@link RedisConnection#serverCommands()}. */
    @Override
    @Deprecated
    default void migrate(byte[] key, RedisNode target, int dbIndex, @Nullable MigrateOption option, long timeout) {
        serverCommands().migrate(key, target, dbIndex, option, timeout);
    }
}
