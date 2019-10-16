package cn.choleece.base.framework.redis.connection;

import org.springframework.lang.Nullable;

import java.util.List;

/**
 * @description: TODO
 * @author: sf
 * @time: 2019-10-15 09:41
 */
public interface RedisListCommands {

    @Nullable
    Long rPush(byte[] var1, byte[]... var2);

    @Nullable
    Long lPush(byte[] var1, byte[]... var2);

    @Nullable
    Long rPushX(byte[] var1, byte[] var2);

    @Nullable
    Long lPushX(byte[] var1, byte[] var2);

    @Nullable
    Long lLen(byte[] var1);

    @Nullable
    List<byte[]> lRange(byte[] var1, long var2, long var4);

    void lTrim(byte[] var1, long var2, long var4);

    @Nullable
    byte[] lIndex(byte[] var1, long var2);

    @Nullable
    Long lInsert(byte[] var1, RedisListCommands.Position var2, byte[] var3, byte[] var4);

    void lSet(byte[] var1, long var2, byte[] var4);

    @Nullable
    Long lRem(byte[] var1, long var2, byte[] var4);

    @Nullable
    byte[] lPop(byte[] var1);

    @Nullable
    byte[] rPop(byte[] var1);

    @Nullable
    List<byte[]> bLPop(int var1, byte[]... var2);

    @Nullable
    List<byte[]> bRPop(int var1, byte[]... var2);

    @Nullable
    byte[] rPopLPush(byte[] var1, byte[] var2);

    @Nullable
    byte[] bRPopLPush(int var1, byte[] var2, byte[] var3);

    public static enum Position {
        BEFORE,
        AFTER;

        private Position() {
        }
    }
}
