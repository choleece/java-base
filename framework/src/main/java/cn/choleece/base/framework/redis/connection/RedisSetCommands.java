package cn.choleece.base.framework.redis.connection;

import cn.choleece.base.framework.redis.core.Cursor;
import cn.choleece.base.framework.redis.core.ScanOptions;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Set;

/**
 * @description: Set 命令
 * @author: sf
 * @time: 2019-10-15 10:27
 */
public interface RedisSetCommands {

    @Nullable
    Long sAdd(byte[] var1, byte[]... var2);

    @Nullable
    Long sRem(byte[] var1, byte[]... var2);

    @Nullable
    byte[] sPop(byte[] var1);

    @Nullable
    List<byte[]> sPop(byte[] var1, long var2);

    @Nullable
    Boolean sMove(byte[] var1, byte[] var2, byte[] var3);

    @Nullable
    Long sCard(byte[] var1);

    @Nullable
    Boolean sIsMember(byte[] var1, byte[] var2);

    @Nullable
    Set<byte[]> sInter(byte[]... var1);

    @Nullable
    Long sInterStore(byte[] var1, byte[]... var2);

    @Nullable
    Set<byte[]> sUnion(byte[]... var1);

    @Nullable
    Long sUnionStore(byte[] var1, byte[]... var2);

    @Nullable
    Set<byte[]> sDiff(byte[]... var1);

    @Nullable
    Long sDiffStore(byte[] var1, byte[]... var2);

    @Nullable
    Set<byte[]> sMembers(byte[] var1);

    @Nullable
    byte[] sRandMember(byte[] var1);

    @Nullable
    List<byte[]> sRandMember(byte[] var1, long var2);

    Cursor<byte[]> sScan(byte[] var1, ScanOptions var2);
}
