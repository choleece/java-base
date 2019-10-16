package cn.choleece.base.framework.redis.connection;

import cn.choleece.base.framework.redis.core.Cursor;
import cn.choleece.base.framework.redis.core.ScanOptions;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @description: Hash 命令
 * @author: sf
 * @time: 2019-10-15 10:18
 */
public interface RedisHashCommands {

    @Nullable
    Boolean hSet(byte[] var1, byte[] var2, byte[] var3);

    @Nullable
    Boolean hSetNX(byte[] var1, byte[] var2, byte[] var3);

    @Nullable
    byte[] hGet(byte[] var1, byte[] var2);

    @Nullable
    List<byte[]> hMGet(byte[] var1, byte[]... var2);

    void hMSet(byte[] var1, Map<byte[], byte[]> var2);

    @Nullable
    Long hIncrBy(byte[] var1, byte[] var2, long var3);

    @Nullable
    Double hIncrBy(byte[] var1, byte[] var2, double var3);

    @Nullable
    Boolean hExists(byte[] var1, byte[] var2);

    @Nullable
    Long hDel(byte[] var1, byte[]... var2);

    @Nullable
    Long hLen(byte[] var1);

    @Nullable
    Set<byte[]> hKeys(byte[] var1);

    @Nullable
    List<byte[]> hVals(byte[] var1);

    @Nullable
    Map<byte[], byte[]> hGetAll(byte[] var1);

    Cursor<Map.Entry<byte[], byte[]>> hScan(byte[] var1, ScanOptions var2);

    @Nullable
    Long hStrLen(byte[] var1, byte[] var2);
}
