package cn.choleece.base.framework.redis.connection.jedis;

import cn.choleece.base.framework.data.domain.Range;
import cn.choleece.base.framework.redis.connection.BitFieldSubCommands;
import cn.choleece.base.framework.redis.connection.RedisStringCommands;
import cn.choleece.base.framework.redis.connection.convert.Converters;
import cn.choleece.base.framework.redis.core.types.Expiration;
import cn.choleece.base.framework.redis.util.ByteUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import redis.clients.jedis.BitPosParams;
import redis.clients.jedis.Client;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @description: RequiredArgsConstructor: 会生成一个包含常量，和标识了NotNull的变量的构造方法。生成的构造方法是私有的private。
 * 主要使用前两个注解，这样就不需要自己写构造方法，代码简洁规范
 * 参考：https://blog.csdn.net/qq_37192800/article/details/79785906
 * @author: sf
 * @time: 2019-10-16 14:55
 */
@RequiredArgsConstructor
public class JedisStringCommands implements RedisStringCommands {

    private final @NonNull JedisConnection connection;

    /**
     * 通过key 获取value
     * @param key
     * @return
     */
    @Override
    public byte[] get(byte[] key) {

        Assert.notNull(key, "Key must not be null!");

        try {
            if (isPipelined()) {
                pipeline(connection.newJedisResult(connection.getRequiredPipeline().get(key)));
                return null;
            }
            if (isQueueing()) {
                transaction(connection.newJedisResult(connection.getRequiredTransaction().get(key)));
                return null;
            }

            return connection.getJedis().get(key);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override
    public byte[] getSet(byte[] key, byte[] value) {

        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(value, "Value must not be null!");

        try {
            if (isPipelined()) {
                pipeline(connection.newJedisResult(connection.getRequiredPipeline().getSet(key, value)));
                return null;
            }
            if (isQueueing()) {
                transaction(connection.newJedisResult(connection.getRequiredTransaction().getSet(key, value)));
                return null;
            }
            return connection.getJedis().getSet(key, value);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override
    public List<byte[]> mGet(byte[]... keys) {

        Assert.notNull(keys, "Keys must not be null!");
        Assert.noNullElements(keys, "Keys must not contain null elements!");

        try {
            if (isPipelined()) {
                pipeline(connection.newJedisResult(connection.getRequiredPipeline().mget(keys)));
                return null;
            }
            if (isQueueing()) {
                transaction(connection.newJedisResult(connection.getRequiredTransaction().mget(keys)));
                return null;
            }
            return connection.getJedis().mget(keys);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override
    public Boolean set(byte[] key, byte[] value) {

        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(value, "Value must not be null!");

        try {
            if (isPipelined()) {
                pipeline(connection.newJedisResult(connection.getRequiredPipeline().set(key, value),
                        Converters.stringToBooleanConverter()));
                return null;
            }
            if (isQueueing()) {
                transaction(connection.newJedisResult(connection.getRequiredTransaction().set(key, value),
                        Converters.stringToBooleanConverter()));
                return null;
            }
            return Converters.stringToBoolean(connection.getJedis().set(key, value));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override
    public Boolean set(byte[] key, byte[] value, Expiration expiration, SetOption option) {

        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(value, "Value must not be null!");
        Assert.notNull(expiration, "Expiration must not be null!");
        Assert.notNull(option, "Option must not be null!");

        if (expiration.isPersistent()) {

            if (ObjectUtils.nullSafeEquals(SetOption.UPSERT, option)) {
                return set(key, value);
            } else {

                try {

                    byte[] nxxx = JedisConverters.toSetCommandNxXxArgument(option);

                    if (isPipelined()) {

                        pipeline(connection.newJedisResult(connection.getRequiredPipeline().set(key, value, nxxx),
                                Converters.stringToBooleanConverter(), () -> false));
                        return null;
                    }
                    if (isQueueing()) {

                        transaction(connection.newJedisResult(connection.getRequiredTransaction().set(key, value, nxxx),
                                Converters.stringToBooleanConverter(), () -> false));
                        return null;
                    }

                    return Converters.stringToBoolean(connection.getJedis().set(key, value, nxxx));
                } catch (Exception ex) {
                    throw convertJedisAccessException(ex);
                }
            }

        } else {

            if (ObjectUtils.nullSafeEquals(SetOption.UPSERT, option)) {

                if (ObjectUtils.nullSafeEquals(TimeUnit.MILLISECONDS, expiration.getTimeUnit())) {
                    return pSetEx(key, expiration.getExpirationTime(), value);
                } else {
                    return setEx(key, expiration.getExpirationTime(), value);
                }
            } else {

                byte[] nxxx = JedisConverters.toSetCommandNxXxArgument(option);
                byte[] expx = JedisConverters.toSetCommandExPxArgument(expiration);

                try {
                    if (isPipelined()) {

                        if (expiration.getExpirationTime() > Integer.MAX_VALUE) {

                            throw new IllegalArgumentException(
                                    "Expiration.expirationTime must be less than Integer.MAX_VALUE for pipeline in Jedis.");
                        }

                        pipeline(connection.newJedisResult(
                                connection.getRequiredPipeline().set(key, value, nxxx, expx, (int) expiration.getExpirationTime()),
                                Converters.stringToBooleanConverter(), () -> false));
                        return null;
                    }
                    if (isQueueing()) {

                        if (expiration.getExpirationTime() > Integer.MAX_VALUE) {
                            throw new IllegalArgumentException(
                                    "Expiration.expirationTime must be less than Integer.MAX_VALUE for transactions in Jedis.");
                        }

                        transaction(connection.newJedisResult(
                                connection.getRequiredTransaction().set(key, value, nxxx, expx, (int) expiration.getExpirationTime()),
                                Converters.stringToBooleanConverter(), () -> false));
                        return null;
                    }

                    return Converters
                            .stringToBoolean(connection.getJedis().set(key, value, nxxx, expx, expiration.getExpirationTime()));

                } catch (Exception ex) {
                    throw convertJedisAccessException(ex);
                }
            }
        }
    }

    @Override
    public Boolean setNX(byte[] key, byte[] value) {

        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(value, "Value must not be null!");

        try {
            if (isPipelined()) {
                pipeline(connection.newJedisResult(connection.getRequiredPipeline().setnx(key, value),
                        JedisConverters.longToBoolean()));
                return null;
            }
            if (isQueueing()) {
                transaction(connection.newJedisResult(connection.getRequiredTransaction().setnx(key, value),
                        JedisConverters.longToBoolean()));
                return null;
            }
            return JedisConverters.toBoolean(connection.getJedis().setnx(key, value));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override
    public Boolean setEx(byte[] key, long seconds, byte[] value) {

        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(value, "Value must not be null!");

        if (seconds > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Time must be less than Integer.MAX_VALUE for setEx in Jedis.");
        }

        try {
            if (isPipelined()) {
                pipeline(connection.newJedisResult(connection.getRequiredPipeline().setex(key, (int) seconds, value),
                        Converters.stringToBooleanConverter(), () -> false));
                return null;
            }
            if (isQueueing()) {
                transaction(connection.newJedisResult(connection.getRequiredTransaction().setex(key, (int) seconds, value),
                        Converters.stringToBooleanConverter(), () -> false));
                return null;
            }
            return Converters.stringToBoolean(connection.getJedis().setex(key, (int) seconds, value));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override
    public Boolean pSetEx(byte[] key, long milliseconds, byte[] value) {

        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(value, "Value must not be null!");

        try {
            if (isPipelined()) {
                pipeline(connection.newJedisResult(connection.getRequiredPipeline().psetex(key, milliseconds, value),
                        Converters.stringToBooleanConverter(), () -> false));
                return null;
            }
            if (isQueueing()) {
                transaction(connection.newJedisResult(connection.getRequiredTransaction().psetex(key, milliseconds, value),
                        Converters.stringToBooleanConverter(), () -> false));
                return null;
            }
            return Converters.stringToBoolean(connection.getJedis().psetex(key, milliseconds, value));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override
    public Boolean mSet(Map<byte[], byte[]> tuples) {

        Assert.notNull(tuples, "Tuples must not be null!");

        try {
            if (isPipelined()) {
                pipeline(connection.newJedisResult(connection.getRequiredPipeline().mset(JedisConverters.toByteArrays(tuples)),
                        Converters.stringToBooleanConverter()));
                return null;
            }
            if (isQueueing()) {
                transaction(
                        connection.newJedisResult(connection.getRequiredTransaction().mset(JedisConverters.toByteArrays(tuples)),
                                Converters.stringToBooleanConverter()));
                return null;
            }
            return Converters.stringToBoolean(connection.getJedis().mset(JedisConverters.toByteArrays(tuples)));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override
    public Boolean mSetNX(Map<byte[], byte[]> tuples) {

        Assert.notNull(tuples, "Tuples must not be null!");

        try {
            if (isPipelined()) {
                pipeline(
                        connection.newJedisResult(connection.getRequiredPipeline().msetnx(JedisConverters.toByteArrays(tuples)),
                                JedisConverters.longToBoolean()));
                return null;
            }
            if (isQueueing()) {
                transaction(
                        connection.newJedisResult(connection.getRequiredTransaction().msetnx(JedisConverters.toByteArrays(tuples)),
                                JedisConverters.longToBoolean()));
                return null;
            }
            return JedisConverters.toBoolean(connection.getJedis().msetnx(JedisConverters.toByteArrays(tuples)));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override
    public Long incr(byte[] key) {

        Assert.notNull(key, "Key must not be null!");

        try {
            if (isPipelined()) {
                pipeline(connection.newJedisResult(connection.getRequiredPipeline().incr(key)));
                return null;
            }
            if (isQueueing()) {
                transaction(connection.newJedisResult(connection.getRequiredTransaction().incr(key)));
                return null;
            }
            return connection.getJedis().incr(key);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override
    public Long incrBy(byte[] key, long value) {

        Assert.notNull(key, "Key must not be null!");

        try {
            if (isPipelined()) {
                pipeline(connection.newJedisResult(connection.getRequiredPipeline().incrBy(key, value)));
                return null;
            }
            if (isQueueing()) {
                transaction(connection.newJedisResult(connection.getRequiredTransaction().incrBy(key, value)));
                return null;
            }
            return connection.getJedis().incrBy(key, value);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override
    public Double incrBy(byte[] key, double value) {

        Assert.notNull(key, "Key must not be null!");

        try {
            if (isPipelined()) {
                pipeline(connection.newJedisResult(connection.getRequiredPipeline().incrByFloat(key, value)));
                return null;
            }
            if (isQueueing()) {
                transaction(connection.newJedisResult(connection.getRequiredTransaction().incrByFloat(key, value)));
                return null;
            }
            return connection.getJedis().incrByFloat(key, value);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override
    public Long decr(byte[] key) {

        Assert.notNull(key, "Key must not be null!");

        try {
            if (isPipelined()) {
                pipeline(connection.newJedisResult(connection.getRequiredPipeline().decr(key)));
                return null;
            }
            if (isQueueing()) {
                transaction(connection.newJedisResult(connection.getRequiredTransaction().decr(key)));
                return null;
            }
            return connection.getJedis().decr(key);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override
    public Long decrBy(byte[] key, long value) {

        Assert.notNull(key, "Key must not be null!");

        try {
            if (isPipelined()) {
                pipeline(connection.newJedisResult(connection.getRequiredPipeline().decrBy(key, value)));
                return null;
            }
            if (isQueueing()) {
                transaction(connection.newJedisResult(connection.getRequiredTransaction().decrBy(key, value)));
                return null;
            }
            return connection.getJedis().decrBy(key, value);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override
    public Long append(byte[] key, byte[] value) {

        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(value, "Value must not be null!");

        try {
            if (isPipelined()) {
                pipeline(connection.newJedisResult(connection.getRequiredPipeline().append(key, value)));
                return null;
            }
            if (isQueueing()) {
                transaction(connection.newJedisResult(connection.getRequiredTransaction().append(key, value)));
                return null;
            }
            return connection.getJedis().append(key, value);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override
    public byte[] getRange(byte[] key, long start, long end) {

        Assert.notNull(key, "Key must not be null!");

        if (start > Integer.MAX_VALUE || end > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Start and end must be less than Integer.MAX_VALUE for getRange in Jedis.");
        }

        try {
            if (isPipelined()) {
                pipeline(connection.newJedisResult(connection.getRequiredPipeline().substr(key, (int) start, (int) end),
                        JedisConverters.stringToBytes()));
                return null;
            }
            if (isQueueing()) {
                transaction(connection.newJedisResult(connection.getRequiredTransaction().substr(key, (int) start, (int) end),
                        JedisConverters.stringToBytes()));
                return null;
            }
            return connection.getJedis().substr(key, (int) start, (int) end);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override
    public void setRange(byte[] key, byte[] value, long offset) {

        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(value, "Value must not be null!");

        try {
            if (isPipelined()) {
                pipeline(connection.newStatusResult(connection.getRequiredPipeline().setrange(key, offset, value)));
                return;
            }
            if (isQueueing()) {
                transaction(connection.newStatusResult(connection.getRequiredTransaction().setrange(key, offset, value)));
                return;
            }
            connection.getJedis().setrange(key, offset, value);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override
    public Boolean getBit(byte[] key, long offset) {

        Assert.notNull(key, "Key must not be null!");

        try {
            if (isPipelined()) {
                pipeline(connection.newJedisResult(connection.getRequiredPipeline().getbit(key, offset)));
                return null;
            }
            if (isQueueing()) {
                transaction(connection.newJedisResult(connection.getRequiredTransaction().getbit(key, offset)));
                return null;
            }
            // compatibility check for Jedis 2.0.0
            Object getBit = connection.getJedis().getbit(key, offset);
            // Jedis 2.0
            if (getBit instanceof Long) {
                return (((Long) getBit) == 0 ? Boolean.FALSE : Boolean.TRUE);
            }
            // Jedis 2.1
            return ((Boolean) getBit);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override
    public Boolean setBit(byte[] key, long offset, boolean value) {

        Assert.notNull(key, "Key must not be null!");

        try {
            if (isPipelined()) {

                pipeline(connection
                        .newJedisResult(connection.getRequiredPipeline().setbit(key, offset, JedisConverters.toBit(value))));
                return null;
            }
            if (isQueueing()) {
                transaction(connection
                        .newJedisResult(connection.getRequiredTransaction().setbit(key, offset, JedisConverters.toBit(value))));
                return null;
            }
            return connection.getJedis().setbit(key, offset, JedisConverters.toBit(value));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override
    public Long bitCount(byte[] key) {

        Assert.notNull(key, "Key must not be null!");

        try {
            if (isPipelined()) {
                pipeline(connection.newJedisResult(connection.getRequiredPipeline().bitcount(key)));
                return null;
            }
            if (isQueueing()) {
                transaction(connection.newJedisResult(connection.getRequiredTransaction().bitcount(key)));
                return null;
            }
            return connection.getJedis().bitcount(key);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override
    public Long bitCount(byte[] key, long start, long end) {

        Assert.notNull(key, "Key must not be null!");

        try {
            if (isPipelined()) {
                pipeline(connection.newJedisResult(connection.getRequiredPipeline().bitcount(key, start, end)));
                return null;
            }
            if (isQueueing()) {
                transaction(connection.newJedisResult(connection.getRequiredTransaction().bitcount(key, start, end)));
                return null;
            }
            return connection.getJedis().bitcount(key, start, end);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override
    public List<Long> bitField(byte[] key, BitFieldSubCommands subCommands) {

        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(subCommands, "Command must not be null!");

        byte[][] args = ByteUtils.mergeArrays(key, JedisConverters.toBitfieldCommandArguments(subCommands));

        return connection.execute("BITFIELD", args, Client::getIntegerMultiBulkReply, JedisClientUtils::getResponse);
    }

    @Override
    public Long bitOp(BitOperation op, byte[] destination, byte[]... keys) {

        Assert.notNull(op, "BitOperation must not be null!");
        Assert.notNull(destination, "Destination key must not be null!");

        if (op == BitOperation.NOT && keys.length > 1) {
            throw new UnsupportedOperationException("Bitop NOT should only be performed against one key");
        }

        try {
            if (isPipelined()) {
                pipeline(connection
                        .newJedisResult(connection.getRequiredPipeline().bitop(JedisConverters.toBitOp(op), destination, keys)));
                return null;
            }
            if (isQueueing()) {
                transaction(connection
                        .newJedisResult(connection.getRequiredTransaction().bitop(JedisConverters.toBitOp(op), destination, keys)));
                return null;
            }
            return connection.getJedis().bitop(JedisConverters.toBitOp(op), destination, keys);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Nullable
    @Override
    public Long bitPos(byte[] key, boolean bit, Range<Long> range) {

        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(range, "Range must not be null! Use Range.unbounded() instead.");

        BitPosParams params = null;
        if (range.getLowerBound().isBounded()) {
            params = range.getUpperBound().isBounded()
                    ? new BitPosParams(range.getLowerBound().getValue().get(), range.getUpperBound().getValue().get())
                    : new BitPosParams(range.getLowerBound().getValue().get());
        }

        try {
            if (isPipelined()) {

                pipeline(connection.newJedisResult(params != null ? connection.getRequiredPipeline().bitpos(key, bit, params)
                        : connection.getRequiredPipeline().bitpos(key, bit)));
                return null;
            }
            if (isQueueing()) {
                transaction(
                        connection.newJedisResult(params != null ? connection.getRequiredTransaction().bitpos(key, bit, params)
                                : connection.getRequiredTransaction().bitpos(key, bit)));
                return null;
            }
            return params != null ? connection.getJedis().bitpos(key, bit, params) : connection.getJedis().bitpos(key, bit);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override
    public Long strLen(byte[] key) {

        Assert.notNull(key, "Key must not be null!");

        try {
            if (isPipelined()) {
                pipeline(connection.newJedisResult(connection.getRequiredPipeline().strlen(key)));
                return null;
            }
            if (isQueueing()) {
                transaction(connection.newJedisResult(connection.getRequiredTransaction().strlen(key)));
                return null;
            }
            return connection.getJedis().strlen(key);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    private boolean isPipelined() {
        return connection.isPipelined();
    }

    private void pipeline(JedisResult result) {
        connection.pipeline(result);
    }

    private boolean isQueueing() {
        return connection.isQueueing();
    }

    private void transaction(JedisResult result) {
        connection.transaction(result);
    }

    private RuntimeException convertJedisAccessException(Exception ex) {
        return connection.convertJedisAccessException(ex);
    }

}
