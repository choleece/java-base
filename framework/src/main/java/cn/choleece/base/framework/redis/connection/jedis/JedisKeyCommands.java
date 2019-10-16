package cn.choleece.base.framework.redis.connection.jedis;

import cn.choleece.base.framework.redis.connection.DataType;
import cn.choleece.base.framework.redis.connection.RedisKeyCommands;
import cn.choleece.base.framework.redis.connection.SortParameters;
import cn.choleece.base.framework.redis.connection.ValueEncoding;
import cn.choleece.base.framework.redis.connection.convert.Converters;
import cn.choleece.base.framework.redis.core.Cursor;
import cn.choleece.base.framework.redis.core.ScanOptions;
import lombok.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.SortingParams;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-15 20:59
 **/
public class JedisKeyCommands implements RedisKeyCommands {

    @NonNull
    private final JedisConnection connection;

    @Override
    public Boolean exists(byte[] key) {
        Assert.notNull(key, "Key must not be null!");

        try {
            if (this.isPipelined()) {
                this.pipeline(this.connection.newJedisResult(this.connection.getRequiredPipeline().exists(key)));
                return null;
            } else if (this.isQueueing()) {
                this.transaction(this.connection.newJedisResult(this.connection.getRequiredTransaction().exists(key)));
                return null;
            } else {
                return this.connection.getJedis().exists(key);
            }
        } catch (Exception var3) {
            throw this.connection.convertJedisAccessException(var3);
        }
    }

    @Override
    @Nullable
    public Long exists(byte[]... keys) {
        Assert.notNull(keys, "Keys must not be null!");
        Assert.noNullElements(keys, "Keys must not contain null elements!");

        try {
            if (this.isPipelined()) {
                this.pipeline(this.connection.newJedisResult(this.connection.getRequiredPipeline().exists(keys)));
                return null;
            } else if (this.isQueueing()) {
                this.transaction(this.connection.newJedisResult(this.connection.getRequiredTransaction().exists(keys)));
                return null;
            } else {
                return this.connection.getJedis().exists(keys);
            }
        } catch (Exception var3) {
            throw this.connection.convertJedisAccessException(var3);
        }
    }

    @Override
    public Long del(byte[]... keys) {
        Assert.noNullElements(keys, "Keys must not be null!");
        Assert.noNullElements(keys, "Keys must not contain null elements!");

        try {
            if (this.isPipelined()) {
                this.pipeline(this.connection.newJedisResult(this.connection.getRequiredPipeline().del(keys)));
                return null;
            } else if (this.isQueueing()) {
                this.transaction(this.connection.newJedisResult(this.connection.getRequiredTransaction().del(keys)));
                return null;
            } else {
                return this.connection.getJedis().del(keys);
            }
        } catch (Exception var3) {
            throw this.connection.convertJedisAccessException(var3);
        }
    }

    @Override
    @Nullable
    public Long unlink(byte[]... keys) {
        Assert.notNull(keys, "Keys must not be null!");
        return (Long)Long.class.cast(this.connection.execute("UNLINK", keys));
    }

    @Override
    public DataType type(byte[] key) {
        Assert.notNull(key, "Key must not be null!");

        try {
            if (this.isPipelined()) {
                this.pipeline(this.connection.newJedisResult(this.connection.getRequiredPipeline().type(key), JedisConverters.stringToDataType()));
                return null;
            } else if (this.isQueueing()) {
                this.transaction(this.connection.newJedisResult(this.connection.getRequiredTransaction().type(key), JedisConverters.stringToDataType()));
                return null;
            } else {
                return JedisConverters.toDataType(this.connection.getJedis().type(key));
            }
        } catch (Exception var3) {
            throw this.connection.convertJedisAccessException(var3);
        }
    }

    @Override
    @Nullable
    public Long touch(byte[]... keys) {
        Assert.notNull(keys, "Keys must not be null!");
        return (Long)Long.class.cast(this.connection.execute("TOUCH", keys));
    }

    @Override
    public Set<byte[]> keys(byte[] pattern) {
        Assert.notNull(pattern, "Pattern must not be null!");

        try {
            if (this.isPipelined()) {
                this.pipeline(this.connection.newJedisResult(this.connection.getRequiredPipeline().keys(pattern)));
                return null;
            } else if (this.isQueueing()) {
                this.transaction(this.connection.newJedisResult(this.connection.getRequiredTransaction().keys(pattern)));
                return null;
            } else {
                return this.connection.getJedis().keys(pattern);
            }
        } catch (Exception var3) {
            throw this.connection.convertJedisAccessException(var3);
        }
    }

    @Override
    public Cursor<byte[]> scan(ScanOptions options) {
        return this.scan(0L, options != null ? options : ScanOptions.NONE);
    }

    public Cursor<byte[]> scan(long cursorId, ScanOptions options) {
        return (new ScanCursor<byte[]>(cursorId, options) {
            protected ScanIteration<byte[]> doScan(long cursorId, ScanOptions options) {
                if (!JedisKeyCommands.this.isQueueing() && !JedisKeyCommands.this.isPipelined()) {
                    ScanParams params = JedisConverters.toScanParams(options);
                    ScanResult<String> result = JedisKeyCommands.this.connection.getJedis().scan(Long.toString(cursorId), params);
                    return new ScanIteration(Long.valueOf(result.getStringCursor()), JedisConverters.stringListToByteList().convert(result.getResult()));
                } else {
                    throw new UnsupportedOperationException("'SCAN' cannot be called in pipeline / transaction mode.");
                }
            }

            protected void doClose() {
                JedisKeyCommands.this.connection.close();
            }
        }).open();
    }

    @Override
    public byte[] randomKey() {
        try {
            if (this.isPipelined()) {
                this.pipeline(this.connection.newJedisResult(this.connection.getRequiredPipeline().randomKeyBinary()));
                return null;
            } else if (this.isQueueing()) {
                this.transaction(this.connection.newJedisResult(this.connection.getRequiredTransaction().randomKeyBinary()));
                return null;
            } else {
                return this.connection.getJedis().randomBinaryKey();
            }
        } catch (Exception var2) {
            throw this.connection.convertJedisAccessException(var2);
        }
    }

    @Override
    public void rename(byte[] sourceKey, byte[] targetKey) {
        Assert.notNull(sourceKey, "Source key must not be null!");
        Assert.notNull(targetKey, "Target key must not be null!");

        try {
            if (this.isPipelined()) {
                this.pipeline(this.connection.newStatusResult(this.connection.getRequiredPipeline().rename(sourceKey, targetKey)));
            } else if (this.isQueueing()) {
                this.transaction(this.connection.newStatusResult(this.connection.getRequiredTransaction().rename(sourceKey, targetKey)));
            } else {
                this.connection.getJedis().rename(sourceKey, targetKey);
            }
        } catch (Exception var4) {
            throw this.connection.convertJedisAccessException(var4);
        }
    }

    @Override
    public Boolean renameNX(byte[] sourceKey, byte[] targetKey) {
        Assert.notNull(sourceKey, "Source key must not be null!");
        Assert.notNull(targetKey, "Target key must not be null!");

        try {
            if (this.isPipelined()) {
                this.pipeline(this.connection.newJedisResult(this.connection.getRequiredPipeline().renamenx(sourceKey, targetKey), JedisConverters.longToBoolean()));
                return null;
            } else if (this.isQueueing()) {
                this.transaction(this.connection.newJedisResult(this.connection.getRequiredTransaction().renamenx(sourceKey, targetKey), JedisConverters.longToBoolean()));
                return null;
            } else {
                return JedisConverters.toBoolean(this.connection.getJedis().renamenx(sourceKey, targetKey));
            }
        } catch (Exception var4) {
            throw this.connection.convertJedisAccessException(var4);
        }
    }

    @Override
    public Boolean expire(byte[] key, long seconds) {
        Assert.notNull(key, "Key must not be null!");
        if (seconds > 2147483647L) {
            return this.pExpire(key, TimeUnit.SECONDS.toMillis(seconds));
        } else {
            try {
                if (this.isPipelined()) {
                    this.pipeline(this.connection.newJedisResult(this.connection.getRequiredPipeline().expire(key, (int)seconds), JedisConverters.longToBoolean()));
                    return null;
                } else if (this.isQueueing()) {
                    this.transaction(this.connection.newJedisResult(this.connection.getRequiredTransaction().expire(key, (int)seconds), JedisConverters.longToBoolean()));
                    return null;
                } else {
                    return JedisConverters.toBoolean(this.connection.getJedis().expire(key, (int)seconds));
                }
            } catch (Exception var5) {
                throw this.connection.convertJedisAccessException(var5);
            }
        }
    }

    @Override
    public Boolean pExpire(byte[] key, long millis) {
        Assert.notNull(key, "Key must not be null!");

        try {
            if (this.isPipelined()) {
                this.pipeline(this.connection.newJedisResult(this.connection.getRequiredPipeline().pexpire(key, millis), JedisConverters.longToBoolean()));
                return null;
            } else if (this.isQueueing()) {
                this.transaction(this.connection.newJedisResult(this.connection.getRequiredTransaction().pexpire(key, millis), JedisConverters.longToBoolean()));
                return null;
            } else {
                return JedisConverters.toBoolean(this.connection.getJedis().pexpire(key, millis));
            }
        } catch (Exception var5) {
            throw this.connection.convertJedisAccessException(var5);
        }
    }

    @Override
    public Boolean expireAt(byte[] key, long unixTime) {
        Assert.notNull(key, "Key must not be null!");

        try {
            if (this.isPipelined()) {
                this.pipeline(this.connection.newJedisResult(this.connection.getRequiredPipeline().expireAt(key, unixTime), JedisConverters.longToBoolean()));
                return null;
            } else if (this.isQueueing()) {
                this.transaction(this.connection.newJedisResult(this.connection.getRequiredTransaction().expireAt(key, unixTime), JedisConverters.longToBoolean()));
                return null;
            } else {
                return JedisConverters.toBoolean(this.connection.getJedis().expireAt(key, unixTime));
            }
        } catch (Exception var5) {
            throw this.connection.convertJedisAccessException(var5);
        }
    }

    @Override
    public Boolean pExpireAt(byte[] key, long unixTimeInMillis) {
        Assert.notNull(key, "Key must not be null!");

        try {
            if (this.isPipelined()) {
                this.pipeline(this.connection.newJedisResult(this.connection.getRequiredPipeline().pexpireAt(key, unixTimeInMillis), JedisConverters.longToBoolean()));
                return null;
            } else if (this.isQueueing()) {
                this.transaction(this.connection.newJedisResult(this.connection.getRequiredTransaction().pexpireAt(key, unixTimeInMillis), JedisConverters.longToBoolean()));
                return null;
            } else {
                return JedisConverters.toBoolean(this.connection.getJedis().pexpireAt(key, unixTimeInMillis));
            }
        } catch (Exception var5) {
            throw this.connection.convertJedisAccessException(var5);
        }
    }

    @Override
    public Boolean persist(byte[] key) {
        Assert.notNull(key, "Key must not be null!");

        try {
            if (this.isPipelined()) {
                this.pipeline(this.connection.newJedisResult(this.connection.getRequiredPipeline().persist(key), JedisConverters.longToBoolean()));
                return null;
            } else if (this.isQueueing()) {
                this.transaction(this.connection.newJedisResult(this.connection.getRequiredTransaction().persist(key), JedisConverters.longToBoolean()));
                return null;
            } else {
                return JedisConverters.toBoolean(this.connection.getJedis().persist(key));
            }
        } catch (Exception var3) {
            throw this.connection.convertJedisAccessException(var3);
        }
    }

    @Override
    public Boolean move(byte[] key, int dbIndex) {
        Assert.notNull(key, "Key must not be null!");

        try {
            if (this.isPipelined()) {
                this.pipeline(this.connection.newJedisResult(this.connection.getRequiredPipeline().move(key, dbIndex), JedisConverters.longToBoolean()));
                return null;
            } else if (this.isQueueing()) {
                this.transaction(this.connection.newJedisResult(this.connection.getRequiredTransaction().move(key, dbIndex), JedisConverters.longToBoolean()));
                return null;
            } else {
                return JedisConverters.toBoolean(this.connection.getJedis().move(key, dbIndex));
            }
        } catch (Exception var4) {
            throw this.connection.convertJedisAccessException(var4);
        }
    }

    @Override
    public Long ttl(byte[] key) {
        Assert.notNull(key, "Key must not be null!");

        try {
            if (this.isPipelined()) {
                this.pipeline(this.connection.newJedisResult(this.connection.getRequiredPipeline().ttl(key)));
                return null;
            } else if (this.isQueueing()) {
                this.transaction(this.connection.newJedisResult(this.connection.getRequiredTransaction().ttl(key)));
                return null;
            } else {
                return this.connection.getJedis().ttl(key);
            }
        } catch (Exception var3) {
            throw this.connection.convertJedisAccessException(var3);
        }
    }

    @Override
    public Long ttl(byte[] key, TimeUnit timeUnit) {
        Assert.notNull(key, "Key must not be null!");

        try {
            if (this.isPipelined()) {
                this.pipeline(this.connection.newJedisResult(this.connection.getRequiredPipeline().ttl(key), Converters.secondsToTimeUnit(timeUnit)));
                return null;
            } else if (this.isQueueing()) {
                this.transaction(this.connection.newJedisResult(this.connection.getRequiredTransaction().ttl(key), Converters.secondsToTimeUnit(timeUnit)));
                return null;
            } else {
                return Converters.secondsToTimeUnit(this.connection.getJedis().ttl(key), timeUnit);
            }
        } catch (Exception var4) {
            throw this.connection.convertJedisAccessException(var4);
        }
    }

    @Override
    public Long pTtl(byte[] key) {
        Assert.notNull(key, "Key must not be null!");

        try {
            if (this.isPipelined()) {
                this.pipeline(this.connection.newJedisResult(this.connection.getRequiredPipeline().pttl(key)));
                return null;
            } else if (this.isQueueing()) {
                this.transaction(this.connection.newJedisResult(this.connection.getRequiredTransaction().pttl(key)));
                return null;
            } else {
                return this.connection.getJedis().pttl(key);
            }
        } catch (Exception var3) {
            throw this.connection.convertJedisAccessException(var3);
        }
    }

    @Override
    public Long pTtl(byte[] key, TimeUnit timeUnit) {
        Assert.notNull(key, "Key must not be null!");

        try {
            if (this.isPipelined()) {
                this.pipeline(this.connection.newJedisResult(this.connection.getRequiredPipeline().pttl(key), Converters.millisecondsToTimeUnit(timeUnit)));
                return null;
            } else if (this.isQueueing()) {
                this.transaction(this.connection.newJedisResult(this.connection.getRequiredTransaction().pttl(key), Converters.millisecondsToTimeUnit(timeUnit)));
                return null;
            } else {
                return Converters.millisecondsToTimeUnit(this.connection.getJedis().pttl(key), timeUnit);
            }
        } catch (Exception var4) {
            throw this.connection.convertJedisAccessException(var4);
        }
    }

    @Override
    public List<byte[]> sort(byte[] key, SortParameters params) {
        Assert.notNull(key, "Key must not be null!");
        SortingParams sortParams = JedisConverters.toSortingParams(params);

        try {
            if (this.isPipelined()) {
                if (sortParams != null) {
                    this.pipeline(this.connection.newJedisResult(this.connection.getRequiredPipeline().sort(key, sortParams)));
                } else {
                    this.pipeline(this.connection.newJedisResult(this.connection.getRequiredPipeline().sort(key)));
                }

                return null;
            } else if (this.isQueueing()) {
                if (sortParams != null) {
                    this.transaction(this.connection.newJedisResult(this.connection.getRequiredTransaction().sort(key, sortParams)));
                } else {
                    this.transaction(this.connection.newJedisResult(this.connection.getRequiredTransaction().sort(key)));
                }

                return null;
            } else {
                return sortParams != null ? this.connection.getJedis().sort(key, sortParams) : this.connection.getJedis().sort(key);
            }
        } catch (Exception var5) {
            throw this.connection.convertJedisAccessException(var5);
        }
    }

    @Override
    public Long sort(byte[] key, @Nullable SortParameters params, byte[] storeKey) {
        Assert.notNull(key, "Key must not be null!");
        SortingParams sortParams = JedisConverters.toSortingParams(params);

        try {
            if (this.isPipelined()) {
                if (sortParams != null) {
                    this.pipeline(this.connection.newJedisResult(this.connection.getRequiredPipeline().sort(key, sortParams, storeKey)));
                } else {
                    this.pipeline(this.connection.newJedisResult(this.connection.getRequiredPipeline().sort(key, storeKey)));
                }

                return null;
            } else if (this.isQueueing()) {
                if (sortParams != null) {
                    this.transaction(this.connection.newJedisResult(this.connection.getRequiredTransaction().sort(key, sortParams, storeKey)));
                } else {
                    this.transaction(this.connection.newJedisResult(this.connection.getRequiredTransaction().sort(key, storeKey)));
                }

                return null;
            } else {
                return sortParams != null ? this.connection.getJedis().sort(key, sortParams, storeKey) : this.connection.getJedis().sort(key, storeKey);
            }
        } catch (Exception var6) {
            throw this.connection.convertJedisAccessException(var6);
        }
    }

    @Override
    public byte[] dump(byte[] key) {
        Assert.notNull(key, "Key must not be null!");

        try {
            if (this.isPipelined()) {
                this.pipeline(this.connection.newJedisResult(this.connection.getRequiredPipeline().dump(key)));
                return null;
            } else if (this.isQueueing()) {
                this.transaction(this.connection.newJedisResult(this.connection.getRequiredTransaction().dump(key)));
                return null;
            } else {
                return this.connection.getJedis().dump(key);
            }
        } catch (Exception var3) {
            throw this.connection.convertJedisAccessException(var3);
        }
    }

    @Override
    public void restore(byte[] key, long ttlInMillis, byte[] serializedValue, boolean replace) {
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(serializedValue, "Serialized value must not be null!");
        if (replace) {
            this.connection.execute("RESTORE", new byte[][]{key, JedisConverters.toBytes(ttlInMillis), serializedValue, JedisConverters.toBytes("REPLACE")});
        } else if (ttlInMillis > 2147483647L) {
            throw new IllegalArgumentException("TtlInMillis must be less than Integer.MAX_VALUE for restore in Jedis.");
        } else {
            try {
                if (this.isPipelined()) {
                    this.pipeline(this.connection.newStatusResult(this.connection.getRequiredPipeline().restore(key, (int)ttlInMillis, serializedValue)));
                } else if (this.isQueueing()) {
                    this.transaction(this.connection.newStatusResult(this.connection.getRequiredTransaction().restore(key, (int)ttlInMillis, serializedValue)));
                } else {
                    this.connection.getJedis().restore(key, (int)ttlInMillis, serializedValue);
                }
            } catch (Exception var7) {
                throw this.connection.convertJedisAccessException(var7);
            }
        }
    }

    @Override
    @Nullable
    public ValueEncoding encodingOf(byte[] key) {
        Assert.notNull(key, "Key must not be null!");

        try {
            if (this.isPipelined()) {
                this.pipeline(this.connection.newJedisResult(this.connection.getRequiredPipeline().objectEncoding(key), JedisConverters::toEncoding, () -> {
                    return ValueEncoding.RedisValueEncoding.VACANT;
                }));
                return null;
            } else if (this.isQueueing()) {
                this.transaction(this.connection.newJedisResult(this.connection.getRequiredTransaction().objectEncoding(key), JedisConverters::toEncoding, () -> {
                    return ValueEncoding.RedisValueEncoding.VACANT;
                }));
                return null;
            } else {
                return JedisConverters.toEncoding(this.connection.getJedis().objectEncoding(key));
            }
        } catch (Exception var3) {
            throw this.connection.convertJedisAccessException(var3);
        }
    }

    @Override
    @Nullable
    public Duration idletime(byte[] key) {
        Assert.notNull(key, "Key must not be null!");

        try {
            if (this.isPipelined()) {
                this.pipeline(this.connection.newJedisResult(this.connection.getRequiredPipeline().objectIdletime(key), Converters::secondsToDuration));
                return null;
            } else if (this.isQueueing()) {
                this.transaction(this.connection.newJedisResult(this.connection.getRequiredTransaction().objectIdletime(key), Converters::secondsToDuration));
                return null;
            } else {
                return Converters.secondsToDuration(this.connection.getJedis().objectIdletime(key));
            }
        } catch (Exception var3) {
            throw this.connection.convertJedisAccessException(var3);
        }
    }

    @Override
    @Nullable
    public Long refcount(byte[] key) {
        Assert.notNull(key, "Key must not be null!");

        try {
            if (this.isPipelined()) {
                this.pipeline(this.connection.newJedisResult(this.connection.getRequiredPipeline().objectRefcount(key)));
                return null;
            } else if (this.isQueueing()) {
                this.transaction(this.connection.newJedisResult(this.connection.getRequiredTransaction().objectRefcount(key)));
                return null;
            } else {
                return this.connection.getJedis().objectRefcount(key);
            }
        } catch (Exception var3) {
            throw this.connection.convertJedisAccessException(var3);
        }
    }

    private boolean isPipelined() {
        return this.connection.isPipelined();
    }

    private void pipeline(JedisResult result) {
        this.connection.pipeline(result);
    }

    private boolean isQueueing() {
        return this.connection.isQueueing();
    }

    private void transaction(JedisResult result) {
        this.connection.transaction(result);
    }

    public JedisKeyCommands(@NonNull JedisConnection connection) {
        if (connection == null) {
            throw new NullPointerException("connection is marked @NonNull but is null");
        } else {
            this.connection = connection;
        }
    }
}
