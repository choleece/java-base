package cn.choleece.base.framework.redis.connection;

import cn.choleece.base.framework.redis.core.types.Expiration;
import cn.choleece.base.framework.redis.core.types.RedisClientInfo;
import org.springframework.lang.Nullable;
import sun.reflect.generics.tree.ReturnType;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @description: TODO
 * @author: sf
 * @time: 2019-10-15 09:39
 */
public interface StringRedisConnection extends RedisConnection {

    /**
     * String-friendly ZSet tuple.
     */
    interface StringTuple extends RedisZSetCommands.Tuple {
        String getValueAsString();
    }

    /**
     * 'Native' or 'raw' execution of the given command along-side the given arguments. The command is executed as is,
     * with as little 'interpretation' as possible - it is up to the caller to take care of any processing of arguments or
     * the result.
     *
     * @param command Command to execute
     * @param args Possible command arguments (may be null)
     * @return execution result.
     * @see RedisCommands#execute(String, byte[]...)
     */
    Object execute(String command, String... args);

    /**
     * 'Native' or 'raw' execution of the given command along-side the given arguments. The command is executed as is,
     * with as little 'interpretation' as possible - it is up to the caller to take care of any processing of arguments or
     * the result.
     *
     * @param command Command to execute
     * @return execution result.
     * @see RedisCommands#execute(String, byte[]...)
     */
    Object execute(String command);

    /**
     * Determine if given {@code key} exists.
     *
     * @param key must not be {@literal null}.
     * @return
     * @see <a href="https://redis.io/commands/exists">Redis Documentation: EXISTS</a>
     * @see RedisKeyCommands#exists(byte[])
     */
    Boolean exists(String key);

    /**
     * Count how many of the given {@code keys} exist.
     *
     * @param keys must not be {@literal null}.
     * @return
     * @see <a href="https://redis.io/commands/exists">Redis Documentation: EXISTS</a>
     * @see RedisKeyCommands#exists(byte[][])
     * @since 2.1
     */
    @Nullable
    Long exists(String... keys);

    /**
     * Delete given {@code keys}.
     *
     * @param keys must not be {@literal null}.
     * @return The number of keys that were removed.
     * @see <a href="https://redis.io/commands/del">Redis Documentation: DEL</a>
     * @see RedisKeyCommands#del(byte[]...)
     */
    Long del(String... keys);

    /**
     * Unlink the {@code keys} from the keyspace. Unlike with {@link #del(String...)} the actual memory reclaiming here
     * happens asynchronously.
     *
     * @param keys must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/unlink">Redis Documentation: UNLINK</a>
     * @since 2.1
     */
    @Nullable
    Long unlink(String... keys);

    /**
     * Determine the type stored at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @return
     * @see <a href="https://redis.io/commands/type">Redis Documentation: TYPE</a>
     * @see RedisKeyCommands#type(byte[])
     */
    DataType type(String key);

    /**
     * Alter the last access time of given {@code key(s)}.
     *
     * @param keys must not be {@literal null}.
     * @return {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/touch">Redis Documentation: TOUCH</a>
     * @since 2.1
     */
    @Nullable
    Long touch(String... keys);

    /**
     * Find all keys matching the given {@code pattern}.
     *
     * @param pattern must not be {@literal null}.
     * @return
     * @see <a href="https://redis.io/commands/keys">Redis Documentation: KEYS</a>
     * @see RedisKeyCommands#keys(byte[])
     */
    Collection<String> keys(String pattern);

    /**
     * Rename key {@code oleName} to {@code newName}.
     *
     * @param oldName must not be {@literal null}.
     * @param newName must not be {@literal null}.
     * @see <a href="https://redis.io/commands/rename">Redis Documentation: RENAME</a>
     * @see RedisKeyCommands#rename(byte[], byte[])
     */
    void rename(String oldName, String newName);

    /**
     * Rename key {@code oleName} to {@code newName} only if {@code newName} does not exist.
     *
     * @param oldName must not be {@literal null}.
     * @param newName must not be {@literal null}.
     * @return
     * @see <a href="https://redis.io/commands/renamenx">Redis Documentation: RENAMENX</a>
     * @see RedisKeyCommands#renameNX(byte[], byte[])
     */
    Boolean renameNX(String oldName, String newName);

    /**
     * Set time to live for given {@code key} in seconds.
     *
     * @param key must not be {@literal null}.
     * @param seconds
     * @return
     * @see <a href="https://redis.io/commands/expire">Redis Documentation: EXPIRE</a>
     * @see RedisKeyCommands#expire(byte[], long)
     */
    Boolean expire(String key, long seconds);

    /**
     * Set time to live for given {@code key} in milliseconds.
     *
     * @param key must not be {@literal null}.
     * @param millis
     * @return
     * @see <a href="https://redis.io/commands/pexpire">Redis Documentation: PEXPIRE</a>
     * @see RedisKeyCommands#pExpire(byte[], long)
     */
    Boolean pExpire(String key, long millis);

    /**
     * Set the expiration for given {@code key} as a {@literal UNIX} timestamp.
     *
     * @param key must not be {@literal null}.
     * @param unixTime
     * @return
     * @see <a href="https://redis.io/commands/expireat">Redis Documentation: EXPIREAT</a>
     * @see RedisKeyCommands#expireAt(byte[], long)
     */
    Boolean expireAt(String key, long unixTime);

    /**
     * Set the expiration for given {@code key} as a {@literal UNIX} timestamp in milliseconds.
     *
     * @param key must not be {@literal null}.
     * @param unixTimeInMillis
     * @return
     * @see <a href="https://redis.io/commands/pexpireat">Redis Documentation: PEXPIREAT</a>
     * @see RedisKeyCommands#pExpireAt(byte[], long)
     */
    Boolean pExpireAt(String key, long unixTimeInMillis);

    /**
     * Remove the expiration from given {@code key}.
     *
     * @param key must not be {@literal null}.
     * @return
     * @see <a href="https://redis.io/commands/persist">Redis Documentation: PERSIST</a>
     * @see RedisKeyCommands#persist(byte[])
     */
    Boolean persist(String key);

    /**
     * Move given {@code key} to database with {@code index}.
     *
     * @param key must not be {@literal null}.
     * @param dbIndex
     * @return
     * @see <a href="https://redis.io/commands/move">Redis Documentation: MOVE</a>
     * @see RedisKeyCommands#move(byte[], int)
     */
    Boolean move(String key, int dbIndex);

    /**
     * Get the time to live for {@code key} in seconds.
     *
     * @param key must not be {@literal null}.
     * @return
     * @see <a href="https://redis.io/commands/ttl">Redis Documentation: TTL</a>
     * @see RedisKeyCommands#ttl(byte[])
     */
    Long ttl(String key);

    /**
     * Get the time to live for {@code key} in and convert it to the given {@link TimeUnit}.
     *
     * @param key must not be {@literal null}.
     * @param timeUnit must not be {@literal null}.
     * @return
     * @since 1.8
     * @see <a href="https://redis.io/commands/ttl">Redis Documentation: TTL</a>
     * @see RedisKeyCommands#ttl(byte[], TimeUnit)
     */
    Long ttl(String key, TimeUnit timeUnit);

    /**
     * Get the precise time to live for {@code key} in milliseconds.
     *
     * @param key must not be {@literal null}.
     * @return
     * @see <a href="https://redis.io/commands/pttl">Redis Documentation: PTTL</a>
     * @see RedisKeyCommands#pTtl(byte[])
     */
    Long pTtl(String key);

    /**
     * Get the precise time to live for {@code key} in and convert it to the given {@link TimeUnit}.
     *
     * @param key must not be {@literal null}.
     * @param timeUnit must not be {@literal null}.
     * @return
     * @since 1.8
     * @see <a href="https://redis.io/commands/pttl">Redis Documentation: PTTL</a>
     * @see RedisKeyCommands#pTtl(byte[], TimeUnit)
     */
    Long pTtl(String key, TimeUnit timeUnit);

    /**
     * Returns {@code message} via server roundtrip.
     *
     * @param message the message to echo.
     * @return
     * @see <a href="https://redis.io/commands/echo">Redis Documentation: ECHO</a>
     * @see RedisConnectionCommands#echo(byte[])
     */
    String echo(String message);

    /**
     * Sort the elements for {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param params must not be {@literal null}.
     * @return
     * @see <a href="https://redis.io/commands/sort">Redis Documentation: SORT</a>
     */
    List<String> sort(String key, SortParameters params);

    /**
     * Sort the elements for {@code key} and store result in {@code storeKey}.
     *
     * @param key must not be {@literal null}.
     * @param params must not be {@literal null}.
     * @param storeKey must not be {@literal null}.
     * @return
     * @see <a href="https://redis.io/commands/sort">Redis Documentation: SORT</a>
     */
    Long sort(String key, SortParameters params, String storeKey);

    /**
     * Get the type of internal representation used for storing the value at the given {@code key}.
     *
     * @param key must not be {@literal null}.
     * @return {@literal null} if key does not exist or when used in pipeline / transaction.
     * @throws IllegalArgumentException if {@code key} is {@literal null}.
     * @since 2.1
     */
    @Nullable
    ValueEncoding encodingOf(String key);

    /**
     * Get the {@link Duration} since the object stored at the given {@code key} is idle.
     *
     * @param key must not be {@literal null}.
     * @return {@literal null} if key does not exist or when used in pipeline / transaction.
     * @throws IllegalArgumentException if {@code key} is {@literal null}.
     * @since 2.1
     */
    @Nullable
    Duration idletime(String key);

    /**
     * Get the number of references of the value associated with the specified {@code key}.
     *
     * @param key must not be {@literal null}.
     * @return {@literal null} if key does not exist or when used in pipeline / transaction.
     * @throws IllegalArgumentException if {@code key} is {@literal null}.
     * @since 2.1
     */
    @Nullable
    Long refcount(String key);

    // -------------------------------------------------------------------------
    // Methods dealing with values/Redis strings
    // -------------------------------------------------------------------------

    /**
     * Get the value of {@code key}.
     *
     * @param key must not be {@literal null}.
     * @return
     * @see <a href="https://redis.io/commands/get">Redis Documentation: GET</a>
     * @see RedisStringCommands#get(byte[])
     */
    String get(String key);

    /**
     * Set {@code value} of {@code key} and return its old value.
     *
     * @param key must not be {@literal null}.
     * @param value
     * @return
     * @see <a href="https://redis.io/commands/getset">Redis Documentation: GETSET</a>
     * @see RedisStringCommands#getSet(byte[], byte[])
     */
    String getSet(String key, String value);

    /**
     * Get multiple {@code keys}. Values are returned in the order of the requested keys.
     *
     * @param keys must not be {@literal null}.
     * @return
     * @see <a href="https://redis.io/commands/mget">Redis Documentation: MGET</a>
     * @see RedisStringCommands#mGet(byte[]...)
     */
    List<String> mGet(String... keys);

    /**
     * Set {@code value} for {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param value must not be {@literal null}.
     * @see <a href="https://redis.io/commands/set">Redis Documentation: SET</a>
     * @see RedisStringCommands#set(byte[], byte[])
     */
    @Nullable
    Boolean set(String key, String value);

    /**
     * Set {@code value} for {@code key} applying timeouts from {@code expiration} if set and inserting/updating values
     * depending on {@code option}.
     *
     * @param key must not be {@literal null}.
     * @param value must not be {@literal null}.
     * @param expiration can be {@literal null}. Defaulted to {@link Expiration#persistent()}.
     * @param option can be {@literal null}. Defaulted to {@link SetOption#UPSERT}.
     * @since 1.7
     * @see <a href="https://redis.io/commands/set">Redis Documentation: SET</a>
     * @see RedisStringCommands#set(byte[], byte[], Expiration, SetOption)
     */
    @Nullable
    Boolean set(String key, String value, Expiration expiration, SetOption option);

    /**
     * Set {@code value} for {@code key}, only if {@code key} does not exist.
     *
     * @param key must not be {@literal null}.
     * @param value must not be {@literal null}.
     * @return
     * @see <a href="https://redis.io/commands/setnx">Redis Documentation: SETNX</a>
     * @see RedisStringCommands#setNX(byte[], byte[])
     */
    @Nullable
    Boolean setNX(String key, String value);

    /**
     * Set the {@code value} and expiration in {@code seconds} for {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param seconds
     * @param value must not be {@literal null}.
     * @see <a href="https://redis.io/commands/setex">Redis Documentation: SETEX</a>
     * @see RedisStringCommands#setEx(byte[], long, byte[])
     */
    @Nullable
    Boolean setEx(String key, long seconds, String value);

    /**
     * Set the {@code value} and expiration in {@code milliseconds} for {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param milliseconds
     * @param value must not be {@literal null}.
     * @since 1.3
     * @see <a href="https://redis.io/commands/psetex">Redis Documentation: PSETEX</a>
     * @see RedisStringCommands#pSetEx(byte[], long, byte[])
     */
    @Nullable
    Boolean pSetEx(String key, long milliseconds, String value);

    /**
     * Set multiple keys to multiple values using key-value pairs provided in {@code tuple}.
     *
     * @param tuple must not be {@literal null}.
     * @see <a href="https://redis.io/commands/mset">Redis Documentation: MSET</a>
     * @see RedisStringCommands#mSet(Map)
     */
    @Nullable
    Boolean mSetString(Map<String, String> tuple);

    /**
     * Set multiple keys to multiple values using key-value pairs provided in {@code tuple} only if the provided key does
     * not exist.
     *
     * @param tuple must not be {@literal null}.
     * @see <a href="https://redis.io/commands/msetnx">Redis Documentation: MSETNX</a>
     * @see RedisStringCommands#mSetNX(Map)
     */
    Boolean mSetNXString(Map<String, String> tuple);

    /**
     * Increment an integer value stored as string value of {@code key} by 1.
     *
     * @param key must not be {@literal null}.
     * @return
     * @see <a href="https://redis.io/commands/incr">Redis Documentation: INCR</a>
     * @see RedisStringCommands#incr(byte[])
     */
    Long incr(String key);

    /**
     * Increment an integer value stored of {@code key} by {@code delta}.
     *
     * @param key must not be {@literal null}.
     * @param value
     * @return
     * @see <a href="https://redis.io/commands/incrby">Redis Documentation: INCRBY</a>
     * @see RedisStringCommands#incrBy(byte[], long)
     */
    Long incrBy(String key, long value);

    /**
     * Increment a floating point number value of {@code key} by {@code delta}.
     *
     * @param key must not be {@literal null}.
     * @param value
     * @return
     * @see <a href="https://redis.io/commands/incrbyfloat">Redis Documentation: INCRBYFLOAT</a>
     * @see RedisStringCommands#incrBy(byte[], double)
     */
    Double incrBy(String key, double value);

    /**
     * Decrement an integer value stored as string value of {@code key} by 1.
     *
     * @param key must not be {@literal null}.
     * @return
     * @see <a href="https://redis.io/commands/decr">Redis Documentation: DECR</a>
     * @see RedisStringCommands#decr(byte[])
     */
    Long decr(String key);

    /**
     * Decrement an integer value stored as string value of {@code key} by {@code value}.
     *
     * @param key must not be {@literal null}.
     * @param value
     * @return
     * @see <a href="https://redis.io/commands/decrby">Redis Documentation: DECRBY</a>
     * @see RedisStringCommands#decrBy(byte[], long)
     */
    Long decrBy(String key, long value);

    /**
     * Append a {@code value} to {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param value
     * @return
     * @see <a href="https://redis.io/commands/append">Redis Documentation: APPEND</a>
     * @see RedisStringCommands#append(byte[], byte[])
     */
    Long append(String key, String value);

    /**
     * Get a substring of value of {@code key} between {@code start} and {@code end}.
     *
     * @param key must not be {@literal null}.
     * @param start
     * @param end
     * @return
     * @see <a href="https://redis.io/commands/getrange">Redis Documentation: GETRANGE</a>
     * @see RedisStringCommands#getRange(byte[], long, long)
     */
    String getRange(String key, long start, long end);

    /**
     * Overwrite parts of {@code key} starting at the specified {@code offset} with given {@code value}.
     *
     * @param key must not be {@literal null}.
     * @param value
     * @param offset
     * @see <a href="https://redis.io/commands/setrange">Redis Documentation: SETRANGE</a>
     * @see RedisStringCommands#setRange(byte[], byte[], long)
     */
    void setRange(String key, String value, long offset);

    /**
     * Get the bit value at {@code offset} of value at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param offset
     * @return
     * @see <a href="https://redis.io/commands/getbit">Redis Documentation: GETBIT</a>
     * @see RedisStringCommands#getBit(byte[], long)
     */
    Boolean getBit(String key, long offset);

    /**
     * Sets the bit at {@code offset} in value stored at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param offset
     * @param value
     * @return the original bit value stored at {@code offset}.
     * @see <a href="https://redis.io/commands/setbit">Redis Documentation: SETBIT</a>
     * @see RedisStringCommands#setBit(byte[], long, boolean)
     */
    Boolean setBit(String key, long offset, boolean value);

    /**
     * Count the number of set bits (population counting) in value stored at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @return
     * @see <a href="https://redis.io/commands/bitcount">Redis Documentation: BITCOUNT</a>
     * @see RedisStringCommands#bitCount(byte[])
     */
    Long bitCount(String key);

    /**
     * Count the number of set bits (population counting) of value stored at {@code key} between {@code start} and
     * {@code end}.
     *
     * @param key must not be {@literal null}.
     * @param start
     * @param end
     * @return
     * @see <a href="https://redis.io/commands/bitcount">Redis Documentation: BITCOUNT</a>
     * @see RedisStringCommands#bitCount(byte[], long, long)
     */
    Long bitCount(String key, long start, long end);

    /**
     * Perform bitwise operations between strings.
     *
     * @param op must not be {@literal null}.
     * @param destination must not be {@literal null}.
     * @param keys must not be {@literal null}.
     * @return
     * @see <a href="https://redis.io/commands/bitop">Redis Documentation: BITOP</a>
     * @see RedisStringCommands#bitOp(BitOperation, byte[], byte[]...)
     */
    Long bitOp(BitOperation op, String destination, String... keys);

    /**
     * Return the position of the first bit set to given {@code bit} in a string.
     *
     * @param key the key holding the actual String.
     * @param bit the bit value to look for.
     * @return {@literal null} when used in pipeline / transaction. The position of the first bit set to 1 or 0 according
     *         to the request.
     * @see <a href="https://redis.io/commands/bitpos">Redis Documentation: BITPOS</a>
     * @since 2.1
     */
    default Long bitPos(String key, boolean bit) {
        return bitPos(key, bit, cn.choleece.base.framework.data.domain.Range.unbounded());
    }

    /**
     * Return the position of the first bit set to given {@code bit} in a string.
     * {@link # org.springframework.data.domain.Range} start and end can contain negative values in order to index
     * <strong>bytes</strong> starting from the end of the string, where {@literal -1} is the last byte, {@literal -2} is
     * the penultimate.
     *
     * @param key the key holding the actual String.
     * @param bit the bit value to look for.
     * @param range must not be {@literal null}. Use {@link # Range # unbounded()} to not limit search.
     * @return {@literal null} when used in pipeline / transaction. The position of the first bit set to 1 or 0 according
     *         to the request.
     * @see <a href="https://redis.io/commands/bitpos">Redis Documentation: BITPOS</a>
     * @since 2.1
     */
    @Nullable
    Long bitPos(String key, boolean bit, cn.choleece.base.framework.data.domain.Range<Long> range);

    /**
     * Get the length of the value stored at {@code key}.
     *
     * @param key must not be {@literal null}.
     * @return
     * @see <a href="https://redis.io/commands/strlen">Redis Documentation: STRLEN</a>
     * @see RedisStringCommands#strLen(byte[])
     */
    Long strLen(String key);

    // -------------------------------------------------------------------------
    // Methods dealing with Redis Pub/Sub
    // -------------------------------------------------------------------------

    /**
     * Publishes the given message to the given channel.
     *
     * @param channel the channel to publish to, must not be {@literal null}.
     * @param message message to publish
     * @return the number of clients that received the message
     * @see <a href="https://redis.io/commands/publish">Redis Documentation: PUBLISH</a>
     * @see RedisPubSubCommands#publish(byte[], byte[])
     */
    Long publish(String channel, String message);

    /**
     * Subscribes the connection to the given channels. Once subscribed, a connection enters listening mode and can only
     * subscribe to other channels or unsubscribe. No other commands are accepted until the connection is unsubscribed.
     * <p>
     * Note that this operation is blocking and the current thread starts waiting for new messages immediately.
     *
     * @param listener message listener, must not be {@literal null}.
     * @param channels channel names, must not be {@literal null}.
     * @see <a href="https://redis.io/commands/subscribe">Redis Documentation: SUBSCRIBE</a>
     * @see RedisPubSubCommands#subscribe(MessageListener, byte[]...)
     */
    void subscribe(MessageListener listener, String... channels);

    /**
     * Subscribes the connection to all channels matching the given patterns. Once subscribed, a connection enters
     * listening mode and can only subscribe to other channels or unsubscribe. No other commands are accepted until the
     * connection is unsubscribed.
     * <p>
     * Note that this operation is blocking and the current thread starts waiting for new messages immediately.
     *
     * @param listener message listener, must not be {@literal null}.
     * @param patterns channel name patterns, must not be {@literal null}.
     * @see <a href="https://redis.io/commands/psubscribe">Redis Documentation: PSUBSCRIBE</a>
     * @see RedisPubSubCommands#pSubscribe(MessageListener, byte[]...)
     */
    void pSubscribe(MessageListener listener, String... patterns);

    // -------------------------------------------------------------------------
    // Methods dealing with Redis Lua Scripting
    // -------------------------------------------------------------------------

    /**
     * Load lua script into scripts cache, without executing it.<br>
     * Execute the script by calling {@link # evalSha(byte[], ReturnType, int, byte[]...)}.
     *
     * @param script must not be {@literal null}.
     * @return
     * @see <a href="https://redis.io/commands/script-load">Redis Documentation: SCRIPT LOAD</a>
     * @see RedisScriptingCommands#scriptLoad(byte[])
     */
    String scriptLoad(String script);

    /**
     * Evaluate given {@code script}.
     *
     * @param script must not be {@literal null}.
     * @param returnType must not be {@literal null}.
     * @param numKeys
     * @param keysAndArgs must not be {@literal null}.
     * @return
     * @see <a href="https://redis.io/commands/eval">Redis Documentation: EVAL</a>
     * @see RedisScriptingCommands eval(byte[], ReturnType, int, byte[]...)
     */
    <T> T eval(String script, ReturnType returnType, int numKeys, String... keysAndArgs);

    /**
     * Evaluate given {@code scriptSha}.
     *
     * @param scriptSha must not be {@literal null}.
     * @param returnType must not be {@literal null}.
     * @param numKeys
     * @param keysAndArgs must not be {@literal null}.
     * @return
     * @see <a href="https://redis.io/commands/evalsha">Redis Documentation: EVALSHA</a>
     * @see RedisScriptingCommands evalSha(String, ReturnType, int, byte[]...)
     */
    <T> T evalSha(String scriptSha, ReturnType returnType, int numKeys, String... keysAndArgs);

    /**
     * Assign given name to current connection.
     *
     * @param name
     * @since 1.3
     * @see <a href="https://redis.io/commands/client-setname">Redis Documentation: CLIENT SETNAME</a>
     * @see RedisServerCommands#setClientName(byte[])
     */
    void setClientName(String name);

    /**
     * Request information and statistics about connected clients.
     *
     * @return {@link List} of {@link RedisClientInfo} objects.
     * @since 1.3
     * @see <a href="https://redis.io/commands/client-list">Redis Documentation: CLIENT LIST</a>
     * @see RedisServerCommands#getClientList()
     */
    List<RedisClientInfo> getClientList();


    /**
     * Get / Manipulate specific integer fields of varying bit widths and arbitrary non (necessary) aligned offset stored
     * at a given {@code key}.
     *
     * @param key must not be {@literal null}.
     * @param command must not be {@literal null}.
     * @return
     */
    List<Long> bitfield(String key, BitFieldSubCommands command);
}
