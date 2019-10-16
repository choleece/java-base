package cn.choleece.base.framework.redis.connection;

import cn.choleece.base.framework.redis.core.Cursor;
import cn.choleece.base.framework.redis.core.ScanOptions;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @description: 字符串命令
 * @author: sf
 * @time: 2019-10-15 09:35
 */
public interface RedisKeyCommands {

    @Nullable
    default Boolean exists(byte[] key) {
        Assert.notNull(key, "Key must not be null!");
        Long count = this.exists(key, null);
        return count != null ? count > 0L : null;
    }

    @Nullable
    Long exists(byte[]... var1);

    @Nullable
    Long del(byte[]... var1);

    @Nullable
    Long unlink(byte[]... var1);

    @Nullable
    DataType type(byte[] var1);

    @Nullable
    Long touch(byte[]... var1);

    @Nullable
    Set<byte[]> keys(byte[] var1);

    Cursor<byte[]> scan(ScanOptions var1);

    @Nullable
    byte[] randomKey();

    void rename(byte[] var1, byte[] var2);

    @Nullable
    Boolean renameNX(byte[] var1, byte[] var2);

    @Nullable
    Boolean expire(byte[] var1, long var2);

    @Nullable
    Boolean pExpire(byte[] var1, long var2);

    @Nullable
    Boolean expireAt(byte[] var1, long var2);

    @Nullable
    Boolean pExpireAt(byte[] var1, long var2);

    @Nullable
    Boolean persist(byte[] var1);

    @Nullable
    Boolean move(byte[] var1, int var2);

    @Nullable
    Long ttl(byte[] var1);

    @Nullable
    Long ttl(byte[] var1, TimeUnit var2);

    @Nullable
    Long pTtl(byte[] var1);

    @Nullable
    Long pTtl(byte[] var1, TimeUnit var2);

    @Nullable
    List<byte[]> sort(byte[] var1, SortParameters var2);

    @Nullable
    Long sort(byte[] var1, SortParameters var2, byte[] var3);

    @Nullable
    byte[] dump(byte[] var1);

    default void restore(byte[] key, long ttlInMillis, byte[] serializedValue) {
        this.restore(key, ttlInMillis, serializedValue, false);
    }

    void restore(byte[] var1, long var2, byte[] var4, boolean var5);

    @Nullable
    ValueEncoding encodingOf(byte[] var1);

    @Nullable
    Duration idletime(byte[] var1);

    @Nullable
    Long refcount(byte[] var1);
}
