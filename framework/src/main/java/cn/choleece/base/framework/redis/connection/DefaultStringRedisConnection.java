package cn.choleece.base.framework.redis.connection;

import cn.choleece.base.framework.redis.RedisSystemException;
import cn.choleece.base.framework.redis.connection.convert.ListConverter;
import cn.choleece.base.framework.redis.connection.convert.MapConverter;
import cn.choleece.base.framework.redis.connection.convert.SetConverter;
import cn.choleece.base.framework.redis.core.Cursor;
import cn.choleece.base.framework.redis.core.ScanOptions;
import cn.choleece.base.framework.redis.core.types.Expiration;
import cn.choleece.base.framework.redis.core.types.RedisClientInfo;
import cn.choleece.base.framework.redis.serializer.RedisSerializer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import sun.reflect.generics.tree.ReturnType;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-15 23:03
 **/
public class DefaultStringRedisConnection implements StringRedisConnection, DecoratedRedisConnection {

    private static final byte[][] EMPTY_2D_BYTE_ARRAY = new byte[0][];

    private final Log log = LogFactory.getLog(DefaultStringRedisConnection.class);
    private final RedisConnection delegate;
    private final RedisSerializer<String> serializer;
    private Converter<byte[], String> bytesToString = new DeserializingConverter();

    @SuppressWarnings("rawtypes")
    private Queue<Converter> pipelineConverters = new LinkedList<>();
    @SuppressWarnings("rawtypes")
    private Queue<Converter> txConverters = new LinkedList<>();
    private boolean deserializePipelineAndTxResults = false;
    private IdentityConverter<Object, ?> identityConverter = new IdentityConverter<>();

    private class DeserializingConverter implements Converter<byte[], String> {
        public String convert(byte[] source) {
            return serializer.deserialize(source);
        }
    }

    private class TupleConverter implements Converter<RedisZSetCommands.Tuple, StringTuple> {
        public StringTuple convert(RedisZSetCommands.Tuple source) {
            return new DefaultStringTuple(source, serializer.deserialize(source.getValue()));
        }
    }

    private class StringTupleConverter implements Converter<StringTuple, RedisZSetCommands.Tuple> {
        public RedisZSetCommands.Tuple convert(StringTuple source) {
            return new DefaultTuple(source.getValue(), source.getScore());
        }
    }

    private class IdentityConverter<S, T> implements Converter<S, T> {
        public Object convert(Object source) {
            return source;
        }
    }

    @SuppressWarnings("rawtypes")
    private class TransactionResultConverter implements Converter<List<Object>, List<Object>> {
        private Queue<Converter> txConverters;

        public TransactionResultConverter(Queue<Converter> txConverters) {
            this.txConverters = txConverters;
        }

        public List<Object> convert(List<Object> execResults) {
            return convertResults(execResults, txConverters);
        }
    }

    public DefaultStringRedisConnection(RedisConnection connection) {
        this(connection, RedisSerializer.string());
    }

    public DefaultStringRedisConnection(RedisConnection connection, RedisSerializer<String> serializer) {

        Assert.notNull(connection, "connection is required");
        Assert.notNull(serializer, "serializer is required");

        this.delegate = connection;
        this.serializer = serializer;
    }

    @Override
    public Long append(byte[] key, byte[] value) {
        return convertAndReturn(delegate.append(key, value), identityConverter);
    }

    @Override
    public void bgSave() {
        delegate.bgSave();
    }

    @Override
    public void bgReWriteAof() {
        delegate.bgReWriteAof();
    }

    @Deprecated
    public void bgWriteAof() {
        bgReWriteAof();
    }

    @Override
    public void close() throws RedisSystemException {
        delegate.close();
    }

    @Override
    public Long dbSize() {
        return convertAndReturn(delegate.dbSize(), identityConverter);
    }

    @Override
    public Long decr(byte[] key) {
        return convertAndReturn(delegate.decr(key), identityConverter);
    }

    @Override
    public Long decrBy(byte[] key, long value) {
        return convertAndReturn(delegate.decrBy(key, value), identityConverter);
    }

    @Override
    public RedisConnection getDelegate() {
        return null;
    }

    @Override
    public Object execute(String command, String... args) {
        return null;
    }

    @Override
    public Object execute(String command) {
        return null;
    }

    @Override
    public Boolean exists(String key) {
        return null;
    }

    @Override
    public Long exists(String... keys) {
        return null;
    }

    @Override
    public Long del(String... keys) {
        return null;
    }

    @Override
    public Long unlink(String... keys) {
        return null;
    }

    @Override
    public DataType type(String key) {
        return null;
    }

    @Override
    public Long touch(String... keys) {
        return null;
    }

    @Override
    public Collection<String> keys(String pattern) {
        return null;
    }

    @Override
    public void rename(String oldName, String newName) {
    }

    @Override
    public Boolean renameNX(String oldName, String newName) {
        return null;
    }

    @Override
    public Boolean expire(String key, long seconds) {
        return null;
    }

    @Override
    public Boolean pExpire(String key, long millis) {
        return null;
    }

    @Override
    public Boolean expireAt(String key, long unixTime) {
        return null;
    }

    @Override
    public Boolean pExpireAt(String key, long unixTimeInMillis) {
        return null;
    }

    @Override
    public Boolean persist(String key) {
        return null;
    }

    @Override
    public Boolean move(String key, int dbIndex) {
        return null;
    }

    @Override
    public Long ttl(String key) {
        return null;
    }

    @Override
    public Long ttl(String key, TimeUnit timeUnit) {
        return null;
    }

    @Override
    public Long pTtl(String key) {
        return null;
    }

    @Override
    public Long pTtl(String key, TimeUnit timeUnit) {
        return null;
    }

    @Override
    public String echo(String message) {
        return null;
    }

    @Override
    public List<String> sort(String key, SortParameters params) {
        return null;
    }

    @Override
    public Long sort(String key, SortParameters params, String storeKey) {
        return null;
    }

    @Override
    public ValueEncoding encodingOf(String key) {
        return null;
    }

    @Override
    public Duration idletime(String key) {
        return null;
    }

    @Override
    public Long refcount(String key) {
        return null;
    }

    @Override
    public String get(String key) {
        return null;
    }

    @Override
    public String getSet(String key, String value) {
        return null;
    }

    @Override
    public List<String> mGet(String... keys) {
        return null;
    }

    @Override
    public Boolean set(String key, String value) {
        return null;
    }

    @Override
    public Boolean set(String key, String value, Expiration expiration, SetOption option) {
        return null;
    }

    @Override
    public Boolean setNX(String key, String value) {
        return null;
    }

    @Override
    public Boolean setEx(String key, long seconds, String value) {
        return null;
    }

    @Override
    public Boolean pSetEx(String key, long milliseconds, String value) {
        return null;
    }

    @Override
    public Boolean mSetString(Map<String, String> tuple) {
        return null;
    }

    @Override
    public Boolean mSetNXString(Map<String, String> tuple) {
        return null;
    }

    @Override
    public Long incr(String key) {
        return null;
    }

    @Override
    public Long incrBy(String key, long value) {
        return null;
    }

    @Override
    public Double incrBy(String key, double value) {
        return null;
    }

    @Override
    public Long decr(String key) {
        return null;
    }

    @Override
    public Long decrBy(String key, long value) {
        return null;
    }

    @Override
    public Long append(String key, String value) {
        return null;
    }

    @Override
    public String getRange(String key, long start, long end) {
        return null;
    }

    @Override
    public void setRange(String key, String value, long offset) {

    }

    @Override
    public Boolean getBit(String key, long offset) {
        return null;
    }

    @Override
    public Boolean setBit(String key, long offset, boolean value) {
        return null;
    }

    @Override
    public Long bitCount(String key) {
        return null;
    }

    @Override
    public Long bitCount(String key, long start, long end) {
        return null;
    }

    @Override
    public Long bitOp(BitOperation op, String destination, String... keys) {
        return null;
    }

    @Override
    public Long bitPos(String key, boolean bit, cn.choleece.base.framework.data.domain.Range<Long> range) {
        return null;
    }

    @Override
    public Long strLen(String key) {
        return null;
    }

    @Override
    public Long lastSave() {
        return null;
    }

    @Override
    public void save() {

    }

    @Override
    public void flushDb() {

    }

    @Override
    public void flushAll() {

    }

    @Override
    public Properties info() {
        return null;
    }

    @Override
    public Properties info(String var1) {
        return null;
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void shutdown(ShutdownOption var1) {

    }

    @Override
    public Properties getConfig(String var1) {
        return null;
    }

    @Override
    public void setConfig(String var1, String var2) {

    }

    @Override
    public void resetConfigStats() {

    }

    @Override
    public Long time() {
        return null;
    }

    @Override
    public void killClient(String var1, int var2) {

    }

    @Override
    public void setClientName(byte[] var1) {

    }

    @Override
    public String getClientName() {
        return null;
    }

    @Override
    public void slaveOf(String var1, int var2) {

    }

    @Override
    public void slaveOfNoOne() {

    }

    @Override
    public void migrate(byte[] var1, RedisNode var2, int var3, MigrateOption var4) {

    }

    @Override
    public void migrate(byte[] var1, RedisNode var2, int var3, MigrateOption var4, long var5) {

    }

    @Override
    public byte[] get(byte[] var1) {
        return new byte[0];
    }

    @Override
    public byte[] getSet(byte[] var1, byte[] var2) {
        return new byte[0];
    }

    @Override
    public List<byte[]> mGet(byte[]... var1) {
        return null;
    }

    @Override
    public Boolean set(byte[] var1, byte[] var2) {
        return null;
    }

    @Override
    public Boolean set(byte[] var1, byte[] var2, Expiration var3, SetOption var4) {
        return null;
    }

    @Override
    public Boolean setNX(byte[] var1, byte[] var2) {
        return null;
    }

    @Override
    public Boolean setEx(byte[] var1, long var2, byte[] var4) {
        return null;
    }

    @Override
    public Boolean pSetEx(byte[] var1, long var2, byte[] var4) {
        return null;
    }

    @Override
    public Boolean mSet(Map<byte[], byte[]> var1) {
        return null;
    }

    @Override
    public Boolean mSetNX(Map<byte[], byte[]> var1) {
        return null;
    }

    @Override
    public Long incr(byte[] var1) {
        return null;
    }

    @Override
    public Long incrBy(byte[] var1, long var2) {
        return null;
    }

    @Override
    public Double incrBy(byte[] var1, double var2) {
        return null;
    }

    @Override
    public byte[] getRange(byte[] var1, long var2, long var4) {
        return new byte[0];
    }

    @Override
    public void setRange(byte[] var1, byte[] var2, long var3) {

    }

    @Override
    public Boolean getBit(byte[] var1, long var2) {
        return null;
    }

    @Override
    public Boolean setBit(byte[] var1, long var2, boolean var4) {
        return null;
    }

    @Override
    public Long bitCount(byte[] var1) {
        return null;
    }

    @Override
    public Long bitCount(byte[] var1, long var2, long var4) {
        return null;
    }

    @Override
    public List<Long> bitField(byte[] var1, BitFieldSubCommands var2) {
        return null;
    }

    @Override
    public Long bitOp(BitOperation var1, byte[] var2, byte[]... var3) {
        return null;
    }

    @Override
    public Long bitPos(byte[] var1, boolean var2, cn.choleece.base.framework.data.domain.Range<Long> var3) {
        return null;
    }

    @Override
    public Long strLen(byte[] var1) {
        return null;
    }

    @Override
    public Long publish(String channel, String message) {
        return null;
    }

    @Override
    public void subscribe(MessageListener listener, String... channels) {

    }

    @Override
    public void pSubscribe(MessageListener listener, String... patterns) {

    }

    @Override
    public String scriptLoad(String script) {
        return null;
    }

    @Override
    public <T> T eval(String script, ReturnType returnType, int numKeys, String... keysAndArgs) {
        return null;
    }

    @Override
    public <T> T evalSha(String scriptSha, ReturnType returnType, int numKeys, String... keysAndArgs) {
        return null;
    }

    @Override
    public void setClientName(String name) {

    }

    @Override
    public List<RedisClientInfo> getClientList() {
        return null;
    }

    @Override
    public List<Long> bitfield(String key, BitFieldSubCommands command) {
        return null;
    }

    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public Object getNativeConnection() {
        return null;
    }

    @Override
    public boolean isQueueing() {
        return false;
    }

    @Override
    public boolean isPipelined() {
        return false;
    }

    @Override
    public void openPipeline() {

    }

    @Override
    public List<Object> closePipeline() throws RedisPipelineException {
        return null;
    }

    @Override
    public RedisSentinelConnection getSentinelConnection() {
        return null;
    }

    @Override
    public Object execute(String command, byte[]... args) {
        return null;
    }

    @Override
    public void select(int dbIndex) {

    }

    @Override
    public byte[] echo(byte[] message) {
        return new byte[0];
    }

    @Override
    public String ping() {
        return null;
    }

    @Override
    public Long exists(byte[]... keys) {
        return null;
    }

    @Override
    public Long del(byte[]... keys) {
        return null;
    }

    @Override
    public Long unlink(byte[]... keys) {
        return null;
    }

    @Override
    public DataType type(byte[] key) {
        return null;
    }

    @Override
    public Long touch(byte[]... keys) {
        return null;
    }

    @Override
    public Set<byte[]> keys(byte[] pattern) {
        return null;
    }

    @Override
    public Cursor<byte[]> scan(ScanOptions options) {
        return null;
    }

    @Override
    public byte[] randomKey() {
        return new byte[0];
    }

    @Override
    public void rename(byte[] sourceKey, byte[] targetKey) {

    }

    @Override
    public Boolean renameNX(byte[] sourceKey, byte[] targetKey) {
        return null;
    }

    @Override
    public Boolean expire(byte[] key, long seconds) {
        return null;
    }

    @Override
    public Boolean pExpire(byte[] key, long millis) {
        return null;
    }

    @Override
    public Boolean expireAt(byte[] key, long unixTime) {
        return null;
    }

    @Override
    public Boolean pExpireAt(byte[] key, long unixTimeInMillis) {
        return null;
    }

    @Override
    public Boolean persist(byte[] key) {
        return null;
    }

    @Override
    public Boolean move(byte[] key, int dbIndex) {
        return null;
    }

    @Override
    public Long ttl(byte[] key) {
        return null;
    }

    @Override
    public Long ttl(byte[] key, TimeUnit timeUnit) {
        return null;
    }

    @Override
    public Long pTtl(byte[] key) {
        return null;
    }

    @Override
    public Long pTtl(byte[] key, TimeUnit timeUnit) {
        return null;
    }

    @Override
    public List<byte[]> sort(byte[] key, SortParameters params) {
        return null;
    }

    @Override
    public Long sort(byte[] key, SortParameters params, byte[] storeKey) {
        return null;
    }

    @Override
    public byte[] dump(byte[] key) {
        return new byte[0];
    }

    @Override
    public void restore(byte[] key, long ttlInMillis, byte[] serializedValue, boolean replace) {

    }

    @Override
    public ValueEncoding encodingOf(byte[] key) {
        return null;
    }

    @Override
    public Duration idletime(byte[] key) {
        return null;
    }

    @Override
    public Long refcount(byte[] key) {
        return null;
    }

    @Override
    public boolean isSubscribed() {
        return false;
    }

    @Override
    public Subscription getSubscription() {
        return null;
    }

    @Override
    public Long publish(byte[] channel, byte[] message) {
        return null;
    }

    @Override
    public void subscribe(MessageListener listener, byte[]... channels) {

    }

    @Override
    public void pSubscribe(MessageListener listener, byte[]... patterns) {

    }

    @SuppressWarnings("unchecked")
    @Nullable
    private <T> T convertAndReturn(@Nullable Object value, Converter converter) {

        if (isFutureConversion()) {

            addResultConverter(converter);
            return null;
        }

        return value == null ? null
                : ObjectUtils.nullSafeEquals(converter, identityConverter) ? (T) value : (T) converter.convert(value);
    }

    private void addResultConverter(Converter<?, ?> converter) {
        if (isQueueing()) {
            txConverters.add(converter);
        } else {
            pipelineConverters.add(converter);
        }
    }

    private boolean isFutureConversion() {
        return isPipelined() || isQueueing();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private List<Object> convertResults(@Nullable List<Object> results, Queue<Converter> converters) {
        if (!deserializePipelineAndTxResults || results == null) {
            return results;
        }
        if (results.size() != converters.size()) {
            // Some of the commands were done directly on the delegate, don't attempt to convert
            log.warn("Delegate returned an unexpected number of results. Abandoning type conversion.");
            return results;
        }
        List<Object> convertedResults = new ArrayList<>(results.size());
        for (Object result : results) {

            Converter converter = converters.remove();
            convertedResults.add(result == null ? null : converter.convert(result));
        }
        return convertedResults;
    }
}
