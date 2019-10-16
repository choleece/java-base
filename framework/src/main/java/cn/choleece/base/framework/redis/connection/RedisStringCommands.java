package cn.choleece.base.framework.redis.connection;

import cn.choleece.base.framework.redis.core.types.Expiration;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Map;

/**
 * @author choleece
 * @Description: 字符串命令
 * @Date 2019-10-14 22:50
 **/
public interface RedisStringCommands {

    @Nullable
    byte[] get(byte[] var1);

    @Nullable
    byte[] getSet(byte[] var1, byte[] var2);

    @Nullable
    List<byte[]> mGet(byte[]... var1);

    @Nullable
    Boolean set(byte[] var1, byte[] var2);

    @Nullable
    Boolean set(byte[] var1, byte[] var2, Expiration var3, RedisStringCommands.SetOption var4);

    @Nullable
    Boolean setNX(byte[] var1, byte[] var2);

    @Nullable
    Boolean setEx(byte[] var1, long var2, byte[] var4);

    @Nullable
    Boolean pSetEx(byte[] var1, long var2, byte[] var4);

    @Nullable
    Boolean mSet(Map<byte[], byte[]> var1);

    @Nullable
    Boolean mSetNX(Map<byte[], byte[]> var1);

    @Nullable
    Long incr(byte[] var1);

    @Nullable
    Long incrBy(byte[] var1, long var2);

    @Nullable
    Double incrBy(byte[] var1, double var2);

    @Nullable
    Long decr(byte[] var1);

    @Nullable
    Long decrBy(byte[] var1, long var2);

    @Nullable
    Long append(byte[] var1, byte[] var2);

    @Nullable
    byte[] getRange(byte[] var1, long var2, long var4);

    void setRange(byte[] var1, byte[] var2, long var3);

    @Nullable
    Boolean getBit(byte[] var1, long var2);

    @Nullable
    Boolean setBit(byte[] var1, long var2, boolean var4);

    @Nullable
    Long bitCount(byte[] var1);

    @Nullable
    Long bitCount(byte[] var1, long var2, long var4);

    @Nullable
    List<Long> bitField(byte[] var1, BitFieldSubCommands var2);

    @Nullable
    Long bitOp(RedisStringCommands.BitOperation var1, byte[] var2, byte[]... var3);

    @Nullable
    Long strLen(byte[] var1);

    public static enum SetOption {
        UPSERT,
        SET_IF_ABSENT,
        SET_IF_PRESENT;

        private SetOption() {
        }

        public static RedisStringCommands.SetOption upsert() {
            return UPSERT;
        }

        public static RedisStringCommands.SetOption ifPresent() {
            return SET_IF_PRESENT;
        }

        public static RedisStringCommands.SetOption ifAbsent() {
            return SET_IF_ABSENT;
        }
    }

    public static enum BitOperation {
        AND,
        OR,
        XOR,
        NOT;

        private BitOperation() {
        }
    }
}
