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
    private final Log log;
    private final RedisConnection delegate;
    private final RedisSerializer<String> serializer;
    private Converter<byte[], String> bytesToString;
    private SetConverter<Tuple, StringTuple> tupleToStringTuple;
    private SetConverter<StringTuple, Tuple> stringTupleToTuple;
    private ListConverter<byte[], String> byteListToStringList;
    private MapConverter<byte[], String> byteMapToStringMap;
    private SetConverter<byte[], String> byteSetToStringSet;
//    private Converter<GeoResults<GeoLocation<byte[]>>, GeoResults<GeoLocation<String>>> byteGeoResultsToStringGeoResults;
    private Queue<Converter> pipelineConverters;
    private Queue<Converter> txConverters;
    private boolean deserializePipelineAndTxResults;
    private DefaultStringRedisConnection.IdentityConverter<Object, ?> identityConverter;

    public DefaultStringRedisConnection(RedisConnection connection) {
        this(connection, RedisSerializer.string());
    }

    public DefaultStringRedisConnection(RedisConnection connection, RedisSerializer<String> serializer) {
        this.log = LogFactory.getLog(DefaultStringRedisConnection.class);
        this.bytesToString = new DefaultStringRedisConnection.DeserializingConverter();
        this.tupleToStringTuple = new SetConverter(new DefaultStringRedisConnection.TupleConverter());
        this.stringTupleToTuple = new SetConverter(new DefaultStringRedisConnection.StringTupleConverter());
        this.byteListToStringList = new ListConverter(this.bytesToString);
        this.byteMapToStringMap = new MapConverter(this.bytesToString);
        this.byteSetToStringSet = new SetConverter(this.bytesToString);
        this.pipelineConverters = new LinkedList();
        this.txConverters = new LinkedList();
        this.deserializePipelineAndTxResults = false;
        this.identityConverter = new DefaultStringRedisConnection.IdentityConverter();
        Assert.notNull(connection, "connection is required");
        Assert.notNull(serializer, "serializer is required");
        this.delegate = connection;
        this.serializer = serializer;
//        this.byteGeoResultsToStringGeoResults = Converters.deserializingGeoResultsConverter(serializer);
    }

    @Override
    public Long append(byte[] key, byte[] value) {
        return (Long)this.convertAndReturn(this.delegate.append(key, value), this.identityConverter);
    }

    @Override
    public void bgSave() {
        this.delegate.bgSave();
    }

    @Override
    public void bgReWriteAof() {
        this.delegate.bgReWriteAof();
    }

    /** @deprecated */
    @Deprecated
    @Override
    public void bgWriteAof() {
        this.bgReWriteAof();
    }

    @Override
    public List<byte[]> bLPop(int timeout, byte[]... keys) {
        return (List)this.convertAndReturn(this.delegate.bLPop(timeout, keys), this.identityConverter);
    }

    @Override
    public List<byte[]> bRPop(int timeout, byte[]... keys) {
        return (List)this.convertAndReturn(this.delegate.bRPop(timeout, keys), this.identityConverter);
    }

    @Override
    public byte[] bRPopLPush(int timeout, byte[] srcKey, byte[] dstKey) {
        return (byte[])this.convertAndReturn(this.delegate.bRPopLPush(timeout, srcKey, dstKey), this.identityConverter);
    }

    @Override
    public void close() throws RedisSystemException {
        this.delegate.close();
    }

    @Override
    public Long dbSize() {
        return (Long)this.convertAndReturn(this.delegate.dbSize(), this.identityConverter);
    }

    @Override
    public Long decr(byte[] key) {
        return (Long)this.convertAndReturn(this.delegate.decr(key), this.identityConverter);
    }

    @Override
    public Long decrBy(byte[] key, long value) {
        return (Long)this.convertAndReturn(this.delegate.decrBy(key, value), this.identityConverter);
    }

    @Override
    public Long del(byte[]... keys) {
        return (Long)this.convertAndReturn(this.delegate.del(keys), this.identityConverter);
    }

    @Override
    public Long unlink(byte[]... keys) {
        return (Long)this.convertAndReturn(this.delegate.unlink(keys), this.identityConverter);
    }

    @Override
    public void discard() {
        try {
            this.delegate.discard();
        } finally {
            this.txConverters.clear();
        }
    }

    @Override
    public byte[] echo(byte[] message) {
        return (byte[])this.convertAndReturn(this.delegate.echo(message), this.identityConverter);
    }

    @Override
    public List<Object> exec() {
        List var2;
        try {
            List<Object> results = this.delegate.exec();
            if (this.isPipelined()) {
                this.pipelineConverters.add(new DefaultStringRedisConnection.TransactionResultConverter(new LinkedList(this.txConverters)));
                var2 = results;
                return var2;
            }

            var2 = this.convertResults(results, this.txConverters);
        } finally {
            this.txConverters.clear();
        }

        return var2;
    }

    @Override
    public Boolean exists(byte[] key) {
        return (Boolean)this.convertAndReturn(this.delegate.exists(key), this.identityConverter);
    }

    @Override
    public Long exists(String... keys) {
        return (Long)this.convertAndReturn(this.delegate.exists(this.serializeMulti(keys)), this.identityConverter);
    }

    @Override
    public Long exists(byte[]... keys) {
        return (Long)this.convertAndReturn(this.delegate.exists(keys), this.identityConverter);
    }

    @Override
    public Boolean expire(byte[] key, long seconds) {
        return (Boolean)this.convertAndReturn(this.delegate.expire(key, seconds), this.identityConverter);
    }

    @Override
    public Boolean expireAt(byte[] key, long unixTime) {
        return (Boolean)this.convertAndReturn(this.delegate.expireAt(key, unixTime), this.identityConverter);
    }

    @Override
    public void flushAll() {
        this.delegate.flushAll();
    }

    @Override
    public void flushDb() {
        this.delegate.flushDb();
    }

    @Override
    public byte[] get(byte[] key) {
        return (byte[])this.convertAndReturn(this.delegate.get(key), this.identityConverter);
    }

    @Override
    public Boolean getBit(byte[] key, long offset) {
        return (Boolean)this.convertAndReturn(this.delegate.getBit(key, offset), this.identityConverter);
    }

    @Override
    public Properties getConfig(String pattern) {
        return (Properties)this.convertAndReturn(this.delegate.getConfig(pattern), this.identityConverter);
    }

    @Override
    public Object getNativeConnection() {
        return this.convertAndReturn(this.delegate.getNativeConnection(), this.identityConverter);
    }

    @Override
    public byte[] getRange(byte[] key, long start, long end) {
        return (byte[])this.convertAndReturn(this.delegate.getRange(key, start, end), this.identityConverter);
    }

    @Override
    public byte[] getSet(byte[] key, byte[] value) {
        return (byte[])this.convertAndReturn(this.delegate.getSet(key, value), this.identityConverter);
    }

    @Override
    public Subscription getSubscription() {
        return this.delegate.getSubscription();
    }

    @Override
    public Long hDel(byte[] key, byte[]... fields) {
        return (Long)this.convertAndReturn(this.delegate.hDel(key, fields), this.identityConverter);
    }

    @Override
    public Boolean hExists(byte[] key, byte[] field) {
        return (Boolean)this.convertAndReturn(this.delegate.hExists(key, field), this.identityConverter);
    }

    @Override
    public byte[] hGet(byte[] key, byte[] field) {
        return (byte[])this.convertAndReturn(this.delegate.hGet(key, field), this.identityConverter);
    }

    @Override
    public Map<byte[], byte[]> hGetAll(byte[] key) {
        return (Map)this.convertAndReturn(this.delegate.hGetAll(key), this.identityConverter);
    }

    @Override
    public Long hIncrBy(byte[] key, byte[] field, long delta) {
        return (Long)this.convertAndReturn(this.delegate.hIncrBy(key, field, delta), this.identityConverter);
    }

    @Override
    public Double hIncrBy(byte[] key, byte[] field, double delta) {
        return (Double)this.convertAndReturn(this.delegate.hIncrBy(key, field, delta), this.identityConverter);
    }

    @Override
    public Set<byte[]> hKeys(byte[] key) {
        return (Set)this.convertAndReturn(this.delegate.hKeys(key), this.identityConverter);
    }

    @Override
    public Long hLen(byte[] key) {
        return (Long)this.convertAndReturn(this.delegate.hLen(key), this.identityConverter);
    }

    @Override
    public List<byte[]> hMGet(byte[] key, byte[]... fields) {
        return (List)this.convertAndReturn(this.delegate.hMGet(key, fields), this.identityConverter);
    }

    @Override
    public void hMSet(byte[] key, Map<byte[], byte[]> hashes) {
        this.delegate.hMSet(key, hashes);
    }

    @Override
    public Boolean hSet(byte[] key, byte[] field, byte[] value) {
        return (Boolean)this.convertAndReturn(this.delegate.hSet(key, field, value), this.identityConverter);
    }

    @Override
    public Boolean hSetNX(byte[] key, byte[] field, byte[] value) {
        return (Boolean)this.convertAndReturn(this.delegate.hSetNX(key, field, value), this.identityConverter);
    }

    @Override
    public List<byte[]> hVals(byte[] key) {
        return (List)this.convertAndReturn(this.delegate.hVals(key), this.identityConverter);
    }

    @Override
    public Long incr(byte[] key) {
        return (Long)this.convertAndReturn(this.delegate.incr(key), this.identityConverter);
    }

    @Override
    public Long incrBy(byte[] key, long value) {
        return (Long)this.convertAndReturn(this.delegate.incrBy(key, value), this.identityConverter);
    }

    @Override
    public Double incrBy(byte[] key, double value) {
        return (Double)this.convertAndReturn(this.delegate.incrBy(key, value), this.identityConverter);
    }

    @Override
    public Properties info() {
        return (Properties)this.convertAndReturn(this.delegate.info(), this.identityConverter);
    }

    @Override
    public Properties info(String section) {
        return (Properties)this.convertAndReturn(this.delegate.info(section), this.identityConverter);
    }

    @Override
    public boolean isClosed() {
        return this.delegate.isClosed();
    }

    @Override
    public boolean isQueueing() {
        return this.delegate.isQueueing();
    }

    @Override
    public boolean isSubscribed() {
        return this.delegate.isSubscribed();
    }

    @Override
    public Set<byte[]> keys(byte[] pattern) {
        return (Set)this.convertAndReturn(this.delegate.keys(pattern), this.identityConverter);
    }

    @Override
    public Long lastSave() {
        return (Long)this.convertAndReturn(this.delegate.lastSave(), this.identityConverter);
    }

    @Override
    public byte[] lIndex(byte[] key, long index) {
        return (byte[])this.convertAndReturn(this.delegate.lIndex(key, index), this.identityConverter);
    }

    @Override
    public Long lInsert(byte[] key, Position where, byte[] pivot, byte[] value) {
        return (Long)this.convertAndReturn(this.delegate.lInsert(key, where, pivot, value), this.identityConverter);
    }

    @Override
    public Long lLen(byte[] key) {
        return (Long)this.convertAndReturn(this.delegate.lLen(key), this.identityConverter);
    }

    @Override
    public byte[] lPop(byte[] key) {
        return (byte[])this.convertAndReturn(this.delegate.lPop(key), this.identityConverter);
    }

    @Override
    public Long lPush(byte[] key, byte[]... values) {
        return (Long)this.convertAndReturn(this.delegate.lPush(key, values), this.identityConverter);
    }

    @Override
    public Long lPushX(byte[] key, byte[] value) {
        return (Long)this.convertAndReturn(this.delegate.lPushX(key, value), this.identityConverter);
    }

    @Override
    public List<byte[]> lRange(byte[] key, long start, long end) {
        return (List)this.convertAndReturn(this.delegate.lRange(key, start, end), this.identityConverter);
    }

    @Override
    public Long lRem(byte[] key, long count, byte[] value) {
        return (Long)this.convertAndReturn(this.delegate.lRem(key, count, value), this.identityConverter);
    }

    @Override
    public void lSet(byte[] key, long index, byte[] value) {
        this.delegate.lSet(key, index, value);
    }

    @Override
    public void lTrim(byte[] key, long start, long end) {
        this.delegate.lTrim(key, start, end);
    }

    @Override
    public List<byte[]> mGet(byte[]... keys) {
        return (List)this.convertAndReturn(this.delegate.mGet(keys), this.identityConverter);
    }

    @Override
    public Boolean mSet(Map<byte[], byte[]> tuple) {
        return (Boolean)this.convertAndReturn(this.delegate.mSet(tuple), this.identityConverter);
    }

    @Override
    public Boolean mSetNX(Map<byte[], byte[]> tuple) {
        return (Boolean)this.convertAndReturn(this.delegate.mSetNX(tuple), this.identityConverter);
    }

    @Override
    public void multi() {
        this.delegate.multi();
    }

    @Override
    public Boolean persist(byte[] key) {
        return (Boolean)this.convertAndReturn(this.delegate.persist(key), this.identityConverter);
    }

    @Override
    public Boolean move(byte[] key, int dbIndex) {
        return (Boolean)this.convertAndReturn(this.delegate.move(key, dbIndex), this.identityConverter);
    }

    @Override
    public String ping() {
        return (String)this.convertAndReturn(this.delegate.ping(), this.identityConverter);
    }

    @Override
    public void pSubscribe(MessageListener listener, byte[]... patterns) {
        this.delegate.pSubscribe(listener, patterns);
    }

    @Override
    public Long publish(byte[] channel, byte[] message) {
        return (Long)this.convertAndReturn(this.delegate.publish(channel, message), this.identityConverter);
    }

    @Override
    public byte[] randomKey() {
        return (byte[])this.convertAndReturn(this.delegate.randomKey(), this.identityConverter);
    }

    @Override
    public void rename(byte[] sourceKey, byte[] targetKey) {
        this.delegate.rename(sourceKey, targetKey);
    }

    @Override
    public Boolean renameNX(byte[] sourceKey, byte[] targetKey) {
        return (Boolean)this.convertAndReturn(this.delegate.renameNX(sourceKey, targetKey), this.identityConverter);
    }

    @Override
    public void resetConfigStats() {
        this.delegate.resetConfigStats();
    }

    @Override
    public byte[] rPop(byte[] key) {
        return (byte[])this.convertAndReturn(this.delegate.rPop(key), this.identityConverter);
    }

    @Override
    public byte[] rPopLPush(byte[] srcKey, byte[] dstKey) {
        return (byte[])this.convertAndReturn(this.delegate.rPopLPush(srcKey, dstKey), this.identityConverter);
    }

    @Override
    public Long rPush(byte[] key, byte[]... values) {
        return (Long)this.convertAndReturn(this.delegate.rPush(key, values), this.identityConverter);
    }

    @Override
    public Long rPushX(byte[] key, byte[] value) {
        return (Long)this.convertAndReturn(this.delegate.rPushX(key, value), this.identityConverter);
    }

    @Override
    public Long sAdd(byte[] key, byte[]... values) {
        return (Long)this.convertAndReturn(this.delegate.sAdd(key, values), this.identityConverter);
    }

    @Override
    public void save() {
        this.delegate.save();
    }

    @Override
    public Long sCard(byte[] key) {
        return (Long)this.convertAndReturn(this.delegate.sCard(key), this.identityConverter);
    }

    @Override
    public Set<byte[]> sDiff(byte[]... keys) {
        return (Set)this.convertAndReturn(this.delegate.sDiff(keys), this.identityConverter);
    }

    @Override
    public Long sDiffStore(byte[] destKey, byte[]... keys) {
        return (Long)this.convertAndReturn(this.delegate.sDiffStore(destKey, keys), this.identityConverter);
    }

    @Override
    public void select(int dbIndex) {
        this.delegate.select(dbIndex);
    }

    @Override
    public Boolean set(byte[] key, byte[] value) {
        return (Boolean)this.convertAndReturn(this.delegate.set(key, value), this.identityConverter);
    }

    public Boolean set(byte[] key, byte[] value, Expiration expiration, SetOption option) {
        return (Boolean)this.convertAndReturn(this.delegate.set(key, value, expiration, option), this.identityConverter);
    }

    @Override
    public Boolean setBit(byte[] key, long offset, boolean value) {
        return (Boolean)this.convertAndReturn(this.delegate.setBit(key, offset, value), this.identityConverter);
    }

    @Override
    public void setConfig(String param, String value) {
        this.delegate.setConfig(param, value);
    }

    @Override
    public Boolean setEx(byte[] key, long seconds, byte[] value) {
        return (Boolean)this.convertAndReturn(this.delegate.setEx(key, seconds, value), this.identityConverter);
    }

    @Override
    public Boolean pSetEx(byte[] key, long milliseconds, byte[] value) {
        return (Boolean)this.convertAndReturn(this.delegate.pSetEx(key, milliseconds, value), this.identityConverter);
    }

    @Override
    public Boolean setNX(byte[] key, byte[] value) {
        return (Boolean)this.convertAndReturn(this.delegate.setNX(key, value), this.identityConverter);
    }

    @Override
    public void setRange(byte[] key, byte[] value, long start) {
        this.delegate.setRange(key, value, start);
    }

    @Override
    public void shutdown() {
        this.delegate.shutdown();
    }

    @Override
    public void shutdown(ShutdownOption option) {
        this.delegate.shutdown(option);
    }

    @Override
    public Set<byte[]> sInter(byte[]... keys) {
        return (Set)this.convertAndReturn(this.delegate.sInter(keys), this.identityConverter);
    }

    @Override
    public Long sInterStore(byte[] destKey, byte[]... keys) {
        return (Long)this.convertAndReturn(this.delegate.sInterStore(destKey, keys), this.identityConverter);
    }

    @Override
    public Boolean sIsMember(byte[] key, byte[] value) {
        return (Boolean)this.convertAndReturn(this.delegate.sIsMember(key, value), this.identityConverter);
    }

    @Override
    public Set<byte[]> sMembers(byte[] key) {
        return (Set)this.convertAndReturn(this.delegate.sMembers(key), this.identityConverter);
    }

    @Override
    public Boolean sMove(byte[] srcKey, byte[] destKey, byte[] value) {
        return (Boolean)this.convertAndReturn(this.delegate.sMove(srcKey, destKey, value), this.identityConverter);
    }

    @Override
    public Long sort(byte[] key, SortParameters params, byte[] storeKey) {
        return (Long)this.convertAndReturn(this.delegate.sort(key, params, storeKey), this.identityConverter);
    }

    @Override
    public List<byte[]> sort(byte[] key, SortParameters params) {
        return (List)this.convertAndReturn(this.delegate.sort(key, params), this.identityConverter);
    }

    @Override
    public ValueEncoding encodingOf(byte[] key) {
        return (ValueEncoding)this.convertAndReturn(this.delegate.encodingOf(key), this.identityConverter);
    }

    @Override
    public Duration idletime(byte[] key) {
        return (Duration)this.convertAndReturn(this.delegate.idletime(key), this.identityConverter);
    }

    @Override
    public Long refcount(byte[] key) {
        return (Long)this.convertAndReturn(this.delegate.refcount(key), this.identityConverter);
    }

    @Override
    public byte[] sPop(byte[] key) {
        return (byte[])this.convertAndReturn(this.delegate.sPop(key), this.identityConverter);
    }

    @Override
    public List<byte[]> sPop(byte[] key, long count) {
        return (List)this.convertAndReturn(this.delegate.sPop(key, count), this.identityConverter);
    }

    @Override
    public byte[] sRandMember(byte[] key) {
        return (byte[])this.convertAndReturn(this.delegate.sRandMember(key), this.identityConverter);
    }

    @Override
    public List<byte[]> sRandMember(byte[] key, long count) {
        return (List)this.convertAndReturn(this.delegate.sRandMember(key, count), this.identityConverter);
    }

    @Override
    public Long sRem(byte[] key, byte[]... values) {
        return (Long)this.convertAndReturn(this.delegate.sRem(key, values), this.identityConverter);
    }

    @Override
    public Long strLen(byte[] key) {
        return (Long)this.convertAndReturn(this.delegate.strLen(key), this.identityConverter);
    }

    @Override
    public Long bitCount(byte[] key) {
        return (Long)this.convertAndReturn(this.delegate.bitCount(key), this.identityConverter);
    }

    @Override
    public Long bitCount(byte[] key, long start, long end) {
        return (Long)this.convertAndReturn(this.delegate.bitCount(key, start, end), this.identityConverter);
    }

    @Override
    public Long bitOp(BitOperation op, byte[] destination, byte[]... keys) {
        return (Long)this.convertAndReturn(this.delegate.bitOp(op, destination, keys), this.identityConverter);
    }

    @Nullable
    public Long bitPos(byte[] key, boolean bit, Range<Long> range) {
        return (Long)this.convertAndReturn(this.delegate.bitPos(key, bit, range), this.identityConverter);
    }

    @Override
    public void subscribe(MessageListener listener, byte[]... channels) {
        this.delegate.subscribe(listener, channels);
    }

    @Override
    public Set<byte[]> sUnion(byte[]... keys) {
        return (Set)this.convertAndReturn(this.delegate.sUnion(keys), this.identityConverter);
    }

    @Override
    public Long sUnionStore(byte[] destKey, byte[]... keys) {
        return (Long)this.convertAndReturn(this.delegate.sUnionStore(destKey, keys), this.identityConverter);
    }

    @Override
    public Long ttl(byte[] key) {
        return (Long)this.convertAndReturn(this.delegate.ttl(key), this.identityConverter);
    }

    @Override
    public Long ttl(byte[] key, TimeUnit timeUnit) {
        return (Long)this.convertAndReturn(this.delegate.ttl(key, timeUnit), this.identityConverter);
    }

    @Override
    public DataType type(byte[] key) {
        return (DataType)this.convertAndReturn(this.delegate.type(key), this.identityConverter);
    }

    @Override
    public Long touch(byte[]... keys) {
        return (Long)this.convertAndReturn(this.delegate.touch(keys), this.identityConverter);
    }

    @Override
    public void unwatch() {
        this.delegate.unwatch();
    }

    @Override
    public void watch(byte[]... keys) {
        this.delegate.watch(keys);
    }

    @Override
    public Boolean zAdd(byte[] key, double score, byte[] value) {
        return (Boolean)this.convertAndReturn(this.delegate.zAdd(key, score, value), this.identityConverter);
    }

    @Override
    public Long zAdd(byte[] key, Set<Tuple> tuples) {
        return (Long)this.convertAndReturn(this.delegate.zAdd(key, tuples), this.identityConverter);
    }

    @Override
    public Long zCard(byte[] key) {
        return (Long)this.convertAndReturn(this.delegate.zCard(key), this.identityConverter);
    }

    @Override
    public Long zCount(byte[] key, double min, double max) {
        return (Long)this.convertAndReturn(this.delegate.zCount(key, min, max), this.identityConverter);
    }

    public Long zCount(byte[] key, RedisZSetCommands.Range range) {
        return (Long)this.convertAndReturn(this.delegate.zCount(key, range), this.identityConverter);
    }

    @Override
    public Double zIncrBy(byte[] key, double increment, byte[] value) {
        return (Double)this.convertAndReturn(this.delegate.zIncrBy(key, increment, value), this.identityConverter);
    }

    @Override
    public Long zInterStore(byte[] destKey, Aggregate aggregate, Weights weights, byte[]... sets) {
        return (Long)this.convertAndReturn(this.delegate.zInterStore(destKey, aggregate, weights, sets), this.identityConverter);
    }

    @Override
    public Long zInterStore(byte[] destKey, byte[]... sets) {
        return (Long)this.convertAndReturn(this.delegate.zInterStore(destKey, sets), this.identityConverter);
    }

    @Override
    public Set<byte[]> zRange(byte[] key, long start, long end) {
        return (Set)this.convertAndReturn(this.delegate.zRange(key, start, end), this.identityConverter);
    }

    @Override
    public Set<byte[]> zRangeByScore(byte[] key, double min, double max, long offset, long count) {
        return (Set)this.convertAndReturn(this.delegate.zRangeByScore(key, min, max, offset, count), this.identityConverter);
    }

    public Set<byte[]> zRangeByScore(byte[] key, RedisZSetCommands.Range range) {
        return (Set)this.convertAndReturn(this.delegate.zRangeByScore(key, range), this.identityConverter);
    }

    public Set<byte[]> zRangeByScore(byte[] key, RedisZSetCommands.Range range, Limit limit) {
        return (Set)this.convertAndReturn(this.delegate.zRangeByScore(key, range, limit), this.identityConverter);
    }

    public Set<Tuple> zRangeByScoreWithScores(byte[] key, RedisZSetCommands.Range range) {
        return (Set)this.convertAndReturn(this.delegate.zRangeByScoreWithScores(key, range), this.identityConverter);
    }

    @Override
    public Set<byte[]> zRangeByScore(byte[] key, double min, double max) {
        return (Set)this.convertAndReturn(this.delegate.zRangeByScore(key, min, max), this.identityConverter);
    }

    @Override
    public Set<Tuple> zRangeByScoreWithScores(byte[] key, double min, double max, long offset, long count) {
        return (Set)this.convertAndReturn(this.delegate.zRangeByScoreWithScores(key, min, max, offset, count), this.identityConverter);
    }

    public Set<Tuple> zRangeByScoreWithScores(byte[] key, RedisZSetCommands.Range range, Limit limit) {
        return (Set)this.convertAndReturn(this.delegate.zRangeByScoreWithScores(key, range, limit), this.identityConverter);
    }

    @Override
    public Set<Tuple> zRangeByScoreWithScores(byte[] key, double min, double max) {
        return (Set)this.convertAndReturn(this.delegate.zRangeByScoreWithScores(key, min, max), this.identityConverter);
    }

    @Override
    public Set<Tuple> zRangeWithScores(byte[] key, long start, long end) {
        return (Set)this.convertAndReturn(this.delegate.zRangeWithScores(key, start, end), this.identityConverter);
    }

    @Override
    public Set<byte[]> zRevRangeByScore(byte[] key, double min, double max, long offset, long count) {
        return (Set)this.convertAndReturn(this.delegate.zRevRangeByScore(key, min, max, offset, count), this.identityConverter);
    }

    public Set<byte[]> zRevRangeByScore(byte[] key, RedisZSetCommands.Range range) {
        return (Set)this.convertAndReturn(this.delegate.zRevRangeByScore(key, range), this.identityConverter);
    }

    @Override
    public Set<byte[]> zRevRangeByScore(byte[] key, double min, double max) {
        return (Set)this.convertAndReturn(this.delegate.zRevRangeByScore(key, min, max), this.identityConverter);
    }

    public Set<byte[]> zRevRangeByScore(byte[] key, org.springframework.data.redis.connection.RedisZSetCommands.Range range, Limit limit) {
        return (Set)this.convertAndReturn(this.delegate.zRevRangeByScore(key, range, limit), this.identityConverter);
    }

    @Override
    public Set<Tuple> zRevRangeByScoreWithScores(byte[] key, double min, double max, long offset, long count) {
        return (Set)this.convertAndReturn(this.delegate.zRevRangeByScoreWithScores(key, min, max, offset, count), this.identityConverter);
    }

    public Set<Tuple> zRevRangeByScoreWithScores(byte[] key, org.springframework.data.redis.connection.RedisZSetCommands.Range range) {
        return (Set)this.convertAndReturn(this.delegate.zRevRangeByScoreWithScores(key, range), this.identityConverter);
    }

    public Set<Tuple> zRevRangeByScoreWithScores(byte[] key, org.springframework.data.redis.connection.RedisZSetCommands.Range range, Limit limit) {
        return (Set)this.convertAndReturn(this.delegate.zRevRangeByScoreWithScores(key, range, limit), this.identityConverter);
    }

    @Override
    public Set<Tuple> zRevRangeByScoreWithScores(byte[] key, double min, double max) {
        return (Set)this.convertAndReturn(this.delegate.zRevRangeByScoreWithScores(key, min, max), this.identityConverter);
    }

    @Override
    public Long zRank(byte[] key, byte[] value) {
        return (Long)this.convertAndReturn(this.delegate.zRank(key, value), this.identityConverter);
    }

    @Override
    public Long zRem(byte[] key, byte[]... values) {
        return (Long)this.convertAndReturn(this.delegate.zRem(key, values), this.identityConverter);
    }

    @Override
    public Long zRemRange(byte[] key, long start, long end) {
        return (Long)this.convertAndReturn(this.delegate.zRemRange(key, start, end), this.identityConverter);
    }

    @Override
    public Long zRemRangeByScore(byte[] key, double min, double max) {
        return (Long)this.convertAndReturn(this.delegate.zRemRangeByScore(key, min, max), this.identityConverter);
    }

    public Long zRemRangeByScore(byte[] key, org.springframework.data.redis.connection.RedisZSetCommands.Range range) {
        return (Long)this.convertAndReturn(this.delegate.zRemRangeByScore(key, range), this.identityConverter);
    }

    @Override
    public Set<byte[]> zRevRange(byte[] key, long start, long end) {
        return (Set)this.convertAndReturn(this.delegate.zRevRange(key, start, end), this.identityConverter);
    }

    @Override
    public Set<Tuple> zRevRangeWithScores(byte[] key, long start, long end) {
        return (Set)this.convertAndReturn(this.delegate.zRevRangeWithScores(key, start, end), this.identityConverter);
    }

    @Override
    public Long zRevRank(byte[] key, byte[] value) {
        return (Long)this.convertAndReturn(this.delegate.zRevRank(key, value), this.identityConverter);
    }

    @Override
    public Double zScore(byte[] key, byte[] value) {
        return (Double)this.convertAndReturn(this.delegate.zScore(key, value), this.identityConverter);
    }

    @Override
    public Long zUnionStore(byte[] destKey, Aggregate aggregate, Weights weights, byte[]... sets) {
        return (Long)this.convertAndReturn(this.delegate.zUnionStore(destKey, aggregate, weights, sets), this.identityConverter);
    }

    @Override
    public Long zUnionStore(byte[] destKey, byte[]... sets) {
        return (Long)this.convertAndReturn(this.delegate.zUnionStore(destKey, sets), this.identityConverter);
    }

    @Override
    public Boolean pExpire(byte[] key, long millis) {
        return (Boolean)this.convertAndReturn(this.delegate.pExpire(key, millis), this.identityConverter);
    }

    @Override
    public Boolean pExpireAt(byte[] key, long unixTimeInMillis) {
        return (Boolean)this.convertAndReturn(this.delegate.pExpireAt(key, unixTimeInMillis), this.identityConverter);
    }

    public Long pTtl(byte[] key) {
        return (Long)this.convertAndReturn(this.delegate.pTtl(key), this.identityConverter);
    }

    @Override
    public Long pTtl(byte[] key, TimeUnit timeUnit) {
        return (Long)this.convertAndReturn(this.delegate.pTtl(key, timeUnit), this.identityConverter);
    }

    @Override
    public byte[] dump(byte[] key) {
        return (byte[])this.convertAndReturn(this.delegate.dump(key), this.identityConverter);
    }

    @Override
    public void restore(byte[] key, long ttlInMillis, byte[] serializedValue, boolean replace) {
        this.delegate.restore(key, ttlInMillis, serializedValue, replace);
    }

    @Override
    public void scriptFlush() {
        this.delegate.scriptFlush();
    }

    @Override
    public void scriptKill() {
        this.delegate.scriptKill();
    }

    @Override
    public String scriptLoad(byte[] script) {
        return (String)this.convertAndReturn(this.delegate.scriptLoad(script), this.identityConverter);
    }

    @Override
    public List<Boolean> scriptExists(String... scriptSha1) {
        return (List)this.convertAndReturn(this.delegate.scriptExists(scriptSha1), this.identityConverter);
    }

    @Override
    public <T> T eval(byte[] script, ReturnType returnType, int numKeys, byte[]... keysAndArgs) {
        return this.convertAndReturn(this.delegate.eval(script, returnType, numKeys, keysAndArgs), this.identityConverter);
    }

    @Override
    public <T> T evalSha(String scriptSha1, ReturnType returnType, int numKeys, byte[]... keysAndArgs) {
        return this.convertAndReturn(this.delegate.evalSha(scriptSha1, returnType, numKeys, keysAndArgs), this.identityConverter);
    }

    @Override
    public <T> T evalSha(byte[] scriptSha1, ReturnType returnType, int numKeys, byte[]... keysAndArgs) {
        return this.convertAndReturn(this.delegate.evalSha(scriptSha1, returnType, numKeys, keysAndArgs), this.identityConverter);
    }

    private byte[] serialize(String data) {
        return this.serializer.serialize(data);
    }

    private byte[][] serializeMulti(String... keys) {
        if (keys == null) {
            return EMPTY_2D_BYTE_ARRAY;
        } else {
            byte[][] ret = new byte[keys.length][];

            for(int i = 0; i < ret.length; ++i) {
                ret[i] = this.serializer.serialize(keys[i]);
            }

            return ret;
        }
    }

    private Map<byte[], byte[]> serialize(Map<String, String> hashes) {
        Map<byte[], byte[]> ret = new LinkedHashMap(hashes.size());
        Iterator var3 = hashes.entrySet().iterator();

        while(var3.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry)var3.next();
            ret.put(this.serializer.serialize(entry.getKey()), this.serializer.serialize(entry.getValue()));
        }

        return ret;
    }

    @Override
    public Long append(String key, String value) {
        return this.append(this.serialize(key), this.serialize(value));
    }

    @Override
    public List<String> bLPop(int timeout, String... keys) {
        return (List)this.convertAndReturn(this.delegate.bLPop(timeout, this.serializeMulti(keys)), this.byteListToStringList);
    }

    @Override
    public List<String> bRPop(int timeout, String... keys) {
        return (List)this.convertAndReturn(this.delegate.bRPop(timeout, this.serializeMulti(keys)), this.byteListToStringList);
    }

    @Override
    public String bRPopLPush(int timeout, String srcKey, String dstKey) {
        return (String)this.convertAndReturn(this.delegate.bRPopLPush(timeout, this.serialize(srcKey), this.serialize(dstKey)), this.bytesToString);
    }

    @Override
    public Long decr(String key) {
        return this.decr(this.serialize(key));
    }

    @Override
    public Long decrBy(String key, long value) {
        return this.decrBy(this.serialize(key), value);
    }

    @Override
    public Long del(String... keys) {
        return this.del(this.serializeMulti(keys));
    }

    @Override
    public Long unlink(String... keys) {
        return this.unlink(this.serializeMulti(keys));
    }

    @Override
    public String echo(String message) {
        return (String)this.convertAndReturn(this.delegate.echo(this.serialize(message)), this.bytesToString);
    }

    @Override
    public Boolean exists(String key) {
        return this.exists(this.serialize(key));
    }

    @Override
    public Boolean expire(String key, long seconds) {
        return this.expire(this.serialize(key), seconds);
    }

    @Override
    public Boolean expireAt(String key, long unixTime) {
        return this.expireAt(this.serialize(key), unixTime);
    }

    @Override
    public String get(String key) {
        return (String)this.convertAndReturn(this.delegate.get(this.serialize(key)), this.bytesToString);
    }

    @Override
    public Boolean getBit(String key, long offset) {
        return this.getBit(this.serialize(key), offset);
    }

    @Override
    public String getRange(String key, long start, long end) {
        return (String)this.convertAndReturn(this.delegate.getRange(this.serialize(key), start, end), this.bytesToString);
    }

    @Override
    public String getSet(String key, String value) {
        return (String)this.convertAndReturn(this.delegate.getSet(this.serialize(key), this.serialize(value)), this.bytesToString);
    }

    @Override
    public Long hDel(String key, String... fields) {
        return this.hDel(this.serialize(key), this.serializeMulti(fields));
    }

    @Override
    public Boolean hExists(String key, String field) {
        return this.hExists(this.serialize(key), this.serialize(field));
    }

    @Override
    public String hGet(String key, String field) {
        return (String)this.convertAndReturn(this.delegate.hGet(this.serialize(key), this.serialize(field)), this.bytesToString);
    }

    @Override
    public Map<String, String> hGetAll(String key) {
        return (Map)this.convertAndReturn(this.delegate.hGetAll(this.serialize(key)), this.byteMapToStringMap);
    }

    @Override
    public Long hIncrBy(String key, String field, long delta) {
        return this.hIncrBy(this.serialize(key), this.serialize(field), delta);
    }

    @Override
    public Double hIncrBy(String key, String field, double delta) {
        return this.hIncrBy(this.serialize(key), this.serialize(field), delta);
    }

    @Override
    public Set<String> hKeys(String key) {
        return (Set)this.convertAndReturn(this.delegate.hKeys(this.serialize(key)), this.byteSetToStringSet);
    }

    @Override
    public Long hLen(String key) {
        return this.hLen(this.serialize(key));
    }

    @Override
    public List<String> hMGet(String key, String... fields) {
        return (List)this.convertAndReturn(this.delegate.hMGet(this.serialize(key), this.serializeMulti(fields)), this.byteListToStringList);
    }

    @Override
    public void hMSet(String key, Map<String, String> hashes) {
        this.delegate.hMSet(this.serialize(key), this.serialize(hashes));
    }

    @Override
    public Boolean hSet(String key, String field, String value) {
        return this.hSet(this.serialize(key), this.serialize(field), this.serialize(value));
    }

    @Override
    public Boolean hSetNX(String key, String field, String value) {
        return this.hSetNX(this.serialize(key), this.serialize(field), this.serialize(value));
    }

    @Override
    public List<String> hVals(String key) {
        return (List)this.convertAndReturn(this.delegate.hVals(this.serialize(key)), this.byteListToStringList);
    }

    @Override
    public Long incr(String key) {
        return this.incr(this.serialize(key));
    }

    @Override
    public Long incrBy(String key, long value) {
        return this.incrBy(this.serialize(key), value);
    }

    @Override
    public Double incrBy(String key, double value) {
        return this.incrBy(this.serialize(key), value);
    }

    @Override
    public Collection<String> keys(String pattern) {
        return (Collection)this.convertAndReturn(this.delegate.keys(this.serialize(pattern)), this.byteSetToStringSet);
    }

    @Override
    public String lIndex(String key, long index) {
        return (String)this.convertAndReturn(this.delegate.lIndex(this.serialize(key), index), this.bytesToString);
    }

    public Long lInsert(String key, Position where, String pivot, String value) {
        return this.lInsert(this.serialize(key), where, this.serialize(pivot), this.serialize(value));
    }

    @Override
    public Long lLen(String key) {
        return this.lLen(this.serialize(key));
    }

    @Override
    public String lPop(String key) {
        return (String)this.convertAndReturn(this.delegate.lPop(this.serialize(key)), this.bytesToString);
    }

    @Override
    public Long lPush(String key, String... values) {
        return this.lPush(this.serialize(key), this.serializeMulti(values));
    }

    @Override
    public Long lPushX(String key, String value) {
        return this.lPushX(this.serialize(key), this.serialize(value));
    }

    @Override
    public List<String> lRange(String key, long start, long end) {
        return (List)this.convertAndReturn(this.delegate.lRange(this.serialize(key), start, end), this.byteListToStringList);
    }

    @Override
    public Long lRem(String key, long count, String value) {
        return this.lRem(this.serialize(key), count, this.serialize(value));
    }

    @Override
    public void lSet(String key, long index, String value) {
        this.delegate.lSet(this.serialize(key), index, this.serialize(value));
    }

    @Override
    public void lTrim(String key, long start, long end) {
        this.delegate.lTrim(this.serialize(key), start, end);
    }

    @Override
    public List<String> mGet(String... keys) {
        return (List)this.convertAndReturn(this.delegate.mGet(this.serializeMulti(keys)), this.byteListToStringList);
    }

    @Override
    public Boolean mSetNXString(Map<String, String> tuple) {
        return this.mSetNX(this.serialize(tuple));
    }

    @Override
    public Boolean mSetString(Map<String, String> tuple) {
        return this.mSet(this.serialize(tuple));
    }

    @Override
    public Boolean persist(String key) {
        return this.persist(this.serialize(key));
    }

    @Override
    public Boolean move(String key, int dbIndex) {
        return this.move(this.serialize(key), dbIndex);
    }

    @Override
    public void pSubscribe(MessageListener listener, String... patterns) {
        this.delegate.pSubscribe(listener, this.serializeMulti(patterns));
    }

    @Override
    public Long publish(String channel, String message) {
        return this.publish(this.serialize(channel), this.serialize(message));
    }

    @Override
    public void rename(String oldName, String newName) {
        this.delegate.rename(this.serialize(oldName), this.serialize(newName));
    }

    @Override
    public Boolean renameNX(String oldName, String newName) {
        return this.renameNX(this.serialize(oldName), this.serialize(newName));
    }

    @Override
    public String rPop(String key) {
        return (String)this.convertAndReturn(this.delegate.rPop(this.serialize(key)), this.bytesToString);
    }

    @Override
    public String rPopLPush(String srcKey, String dstKey) {
        return (String)this.convertAndReturn(this.delegate.rPopLPush(this.serialize(srcKey), this.serialize(dstKey)), this.bytesToString);
    }

    @Override
    public Long rPush(String key, String... values) {
        return this.rPush(this.serialize(key), this.serializeMulti(values));
    }

    @Override
    public Long rPushX(String key, String value) {
        return this.rPushX(this.serialize(key), this.serialize(value));
    }

    @Override
    public Long sAdd(String key, String... values) {
        return this.sAdd(this.serialize(key), this.serializeMulti(values));
    }

    @Override
    public Long sCard(String key) {
        return this.sCard(this.serialize(key));
    }

    @Override
    public Set<String> sDiff(String... keys) {
        return (Set)this.convertAndReturn(this.delegate.sDiff(this.serializeMulti(keys)), this.byteSetToStringSet);
    }

    @Override
    public Long sDiffStore(String destKey, String... keys) {
        return this.sDiffStore(this.serialize(destKey), this.serializeMulti(keys));
    }

    @Override
    public Boolean set(String key, String value) {
        return this.set(this.serialize(key), this.serialize(value));
    }

    @Override
    public Boolean set(String key, String value, Expiration expiration, SetOption option) {
        return this.set(this.serialize(key), this.serialize(value), expiration, option);
    }

    @Override
    public Boolean setBit(String key, long offset, boolean value) {
        return this.setBit(this.serialize(key), offset, value);
    }

    @Override
    public Boolean setEx(String key, long seconds, String value) {
        return this.setEx(this.serialize(key), seconds, this.serialize(value));
    }

    @Override
    public Boolean pSetEx(String key, long seconds, String value) {
        return this.pSetEx(this.serialize(key), seconds, this.serialize(value));
    }

    @Override
    public Boolean setNX(String key, String value) {
        return this.setNX(this.serialize(key), this.serialize(value));
    }

    @Override
    public void setRange(String key, String value, long start) {
        this.delegate.setRange(this.serialize(key), this.serialize(value), start);
    }

    @Override
    public Set<String> sInter(String... keys) {
        return (Set)this.convertAndReturn(this.delegate.sInter(this.serializeMulti(keys)), this.byteSetToStringSet);
    }

    @Override
    public Long sInterStore(String destKey, String... keys) {
        return this.sInterStore(this.serialize(destKey), this.serializeMulti(keys));
    }

    @Override
    public Boolean sIsMember(String key, String value) {
        return this.sIsMember(this.serialize(key), this.serialize(value));
    }

    @Override
    public Set<String> sMembers(String key) {
        return (Set)this.convertAndReturn(this.delegate.sMembers(this.serialize(key)), this.byteSetToStringSet);
    }

    @Override
    public Boolean sMove(String srcKey, String destKey, String value) {
        return this.sMove(this.serialize(srcKey), this.serialize(destKey), this.serialize(value));
    }

    @Override
    public Long sort(String key, SortParameters params, String storeKey) {
        return this.sort(this.serialize(key), params, this.serialize(storeKey));
    }

    @Override
    public List<String> sort(String key, SortParameters params) {
        return (List)this.convertAndReturn(this.delegate.sort(this.serialize(key), params), this.byteListToStringList);
    }

    @Override
    public ValueEncoding encodingOf(String key) {
        return this.encodingOf(this.serialize(key));
    }

    @Override
    public Duration idletime(String key) {
        return this.idletime(this.serialize(key));
    }

    @Override
    public Long refcount(String key) {
        return this.refcount(this.serialize(key));
    }

    @Override
    public String sPop(String key) {
        return (String)this.convertAndReturn(this.delegate.sPop(this.serialize(key)), this.bytesToString);
    }

    @Override
    public List<String> sPop(String key, long count) {
        return (List)this.convertAndReturn(this.delegate.sPop(this.serialize(key), count), this.byteListToStringList);
    }

    @Override
    public String sRandMember(String key) {
        return (String)this.convertAndReturn(this.delegate.sRandMember(this.serialize(key)), this.bytesToString);
    }

    @Override
    public List<String> sRandMember(String key, long count) {
        return (List)this.convertAndReturn(this.delegate.sRandMember(this.serialize(key), count), this.byteListToStringList);
    }

    @Override
    public Long sRem(String key, String... values) {
        return this.sRem(this.serialize(key), this.serializeMulti(values));
    }

    @Override
    public Long strLen(String key) {
        return this.strLen(this.serialize(key));
    }

    @Override
    public Long bitCount(String key) {
        return this.bitCount(this.serialize(key));
    }

    @Override
    public Long bitCount(String key, long start, long end) {
        return this.bitCount(this.serialize(key), start, end);
    }

    @Override
    public Long bitOp(BitOperation op, String destination, String... keys) {
        return this.bitOp(op, this.serialize(destination), this.serializeMulti(keys));
    }

    @Nullable
    public Long bitPos(String key, boolean bit, Range<Long> range) {
        return this.bitPos(this.serialize(key), bit, range);
    }

    @Override
    public void subscribe(MessageListener listener, String... channels) {
        this.delegate.subscribe(listener, this.serializeMulti(channels));
    }

    @Override
    public Set<String> sUnion(String... keys) {
        return (Set)this.convertAndReturn(this.delegate.sUnion(this.serializeMulti(keys)), this.byteSetToStringSet);
    }

    @Override
    public Long sUnionStore(String destKey, String... keys) {
        return this.sUnionStore(this.serialize(destKey), this.serializeMulti(keys));
    }

    @Override
    public Long ttl(String key) {
        return this.ttl(this.serialize(key));
    }

    @Override
    public Long ttl(String key, TimeUnit timeUnit) {
        return this.ttl(this.serialize(key), timeUnit);
    }

    @Override
    public DataType type(String key) {
        return this.type(this.serialize(key));
    }

    @Override
    @Nullable
    public Long touch(String... keys) {
        return this.touch(this.serializeMulti(keys));
    }

    @Override
    public Boolean zAdd(String key, double score, String value) {
        return this.zAdd(this.serialize(key), score, this.serialize(value));
    }

    @Override
    public Long zAdd(String key, Set<StringTuple> tuples) {
        return this.zAdd(this.serialize(key), this.stringTupleToTuple.convert(tuples));
    }

    @Override
    public Long zCard(String key) {
        return this.zCard(this.serialize(key));
    }

    @Override
    public Long zCount(String key, double min, double max) {
        return this.zCount(this.serialize(key), min, max);
    }

    @Override
    public Double zIncrBy(String key, double increment, String value) {
        return this.zIncrBy(this.serialize(key), increment, this.serialize(value));
    }

    @Override
    public Long zInterStore(String destKey, Aggregate aggregate, int[] weights, String... sets) {
        return this.zInterStore((byte[])this.serialize(destKey), aggregate, (int[])weights, (byte[][])this.serializeMulti(sets));
    }

    @Override
    public Long zInterStore(String destKey, String... sets) {
        return this.zInterStore(this.serialize(destKey), this.serializeMulti(sets));
    }

    @Override
    public Set<String> zRange(String key, long start, long end) {
        return (Set)this.convertAndReturn(this.delegate.zRange(this.serialize(key), start, end), this.byteSetToStringSet);
    }

    @Override
    public Set<String> zRangeByScore(String key, double min, double max, long offset, long count) {
        return (Set)this.convertAndReturn(this.delegate.zRangeByScore(this.serialize(key), min, max, offset, count), this.byteSetToStringSet);
    }

    @Override
    public Set<String> zRangeByScore(String key, double min, double max) {
        return (Set)this.convertAndReturn(this.delegate.zRangeByScore(this.serialize(key), min, max), this.byteSetToStringSet);
    }

    @Override
    public Set<StringTuple> zRangeByScoreWithScores(String key, double min, double max, long offset, long count) {
        return (Set)this.convertAndReturn(this.delegate.zRangeByScoreWithScores(this.serialize(key), min, max, offset, count), this.tupleToStringTuple);
    }

    @Override
    public Set<StringTuple> zRangeByScoreWithScores(String key, double min, double max) {
        return (Set)this.convertAndReturn(this.delegate.zRangeByScoreWithScores(this.serialize(key), min, max), this.tupleToStringTuple);
    }

    @Override
    public Set<StringTuple> zRangeWithScores(String key, long start, long end) {
        return (Set)this.convertAndReturn(this.delegate.zRangeWithScores(this.serialize(key), start, end), this.tupleToStringTuple);
    }

    @Override
    public Long zRank(String key, String value) {
        return this.zRank(this.serialize(key), this.serialize(value));
    }

    @Override
    public Long zRem(String key, String... values) {
        return this.zRem(this.serialize(key), this.serializeMulti(values));
    }

    @Override
    public Long zRemRange(String key, long start, long end) {
        return this.zRemRange(this.serialize(key), start, end);
    }

    @Override
    public Long zRemRangeByScore(String key, double min, double max) {
        return this.zRemRangeByScore(this.serialize(key), min, max);
    }

    @Override
    public Set<String> zRevRange(String key, long start, long end) {
        return (Set)this.convertAndReturn(this.delegate.zRevRange(this.serialize(key), start, end), this.byteSetToStringSet);
    }

    @Override
    public Set<StringTuple> zRevRangeWithScores(String key, long start, long end) {
        return (Set)this.convertAndReturn(this.delegate.zRevRangeWithScores(this.serialize(key), start, end), this.tupleToStringTuple);
    }

    @Override
    public Set<String> zRevRangeByScore(String key, double min, double max) {
        return (Set)this.convertAndReturn(this.delegate.zRevRangeByScore(this.serialize(key), min, max), this.byteSetToStringSet);
    }

    @Override
    public Set<StringTuple> zRevRangeByScoreWithScores(String key, double min, double max) {
        return (Set)this.convertAndReturn(this.delegate.zRevRangeByScoreWithScores(this.serialize(key), min, max), this.tupleToStringTuple);
    }

    @Override
    public Set<String> zRevRangeByScore(String key, double min, double max, long offset, long count) {
        return (Set)this.convertAndReturn(this.delegate.zRevRangeByScore(this.serialize(key), min, max, offset, count), this.byteSetToStringSet);
    }

    @Override
    public Set<StringTuple> zRevRangeByScoreWithScores(String key, double min, double max, long offset, long count) {
        return (Set)this.convertAndReturn(this.delegate.zRevRangeByScoreWithScores(this.serialize(key), min, max, offset, count), this.tupleToStringTuple);
    }

    @Override
    public Long zRevRank(String key, String value) {
        return this.zRevRank(this.serialize(key), this.serialize(value));
    }

    @Override
    public Double zScore(String key, String value) {
        return this.zScore(this.serialize(key), this.serialize(value));
    }

    @Override
    public Long zUnionStore(String destKey, Aggregate aggregate, int[] weights, String... sets) {
        return this.zUnionStore((byte[])this.serialize(destKey), aggregate, (int[])weights, (byte[][])this.serializeMulti(sets));
    }

    @Override
    public Long zUnionStore(String destKey, String... sets) {
        return this.zUnionStore(this.serialize(destKey), this.serializeMulti(sets));
    }

//    public Long geoAdd(byte[] key, Point point, byte[] member) {
//        return (Long)this.convertAndReturn(this.delegate.geoAdd(key, point, member), this.identityConverter);
//    }
//
//    public Long geoAdd(byte[] key, GeoLocation<byte[]> location) {
//        return (Long)this.convertAndReturn(this.delegate.geoAdd(key, location), this.identityConverter);
//    }
//
//    public Long geoAdd(String key, Point point, String member) {
//        return this.geoAdd(this.serialize(key), point, this.serialize(member));
//    }
//
//    public Long geoAdd(String key, GeoLocation<String> location) {
//        Assert.notNull(location, "Location must not be null!");
//        return this.geoAdd(key, location.getPoint(), (String)location.getName());
//    }
//
//    public Long geoAdd(byte[] key, Map<byte[], Point> memberCoordinateMap) {
//        return (Long)this.convertAndReturn(this.delegate.geoAdd(key, memberCoordinateMap), this.identityConverter);
//    }
//
//    public Long geoAdd(byte[] key, Iterable<GeoLocation<byte[]>> locations) {
//        return (Long)this.convertAndReturn(this.delegate.geoAdd(key, locations), this.identityConverter);
//    }
//
//    public Long geoAdd(String key, Map<String, Point> memberCoordinateMap) {
//        Assert.notNull(memberCoordinateMap, "MemberCoordinateMap must not be null!");
//        Map<byte[], Point> byteMap = new HashMap();
//        Iterator var4 = memberCoordinateMap.entrySet().iterator();
//
//        while(var4.hasNext()) {
//            Map.Entry<String, Point> entry = (Map.Entry)var4.next();
//            byteMap.put(this.serialize((String)entry.getKey()), entry.getValue());
//        }
//
//        return this.geoAdd((byte[])this.serialize(key), (Map)byteMap);
//    }
//
//    public Long geoAdd(String key, Iterable<GeoLocation<String>> locations) {
//        Assert.notNull(locations, "Locations must not be null!");
//        Map<byte[], Point> byteMap = new HashMap();
//        Iterator var4 = locations.iterator();
//
//        while(var4.hasNext()) {
//            GeoLocation<String> location = (GeoLocation)var4.next();
//            byteMap.put(this.serialize((String)location.getName()), location.getPoint());
//        }
//
//        return this.geoAdd((byte[])this.serialize(key), (Map)byteMap);
//    }
//
//    public Distance geoDist(byte[] key, byte[] member1, byte[] member2) {
//        return (Distance)this.convertAndReturn(this.delegate.geoDist(key, member1, member2), this.identityConverter);
//    }
//
//    public Distance geoDist(String key, String member1, String member2) {
//        return this.geoDist(this.serialize(key), this.serialize(member1), this.serialize(member2));
//    }
//
//    public Distance geoDist(byte[] key, byte[] member1, byte[] member2, Metric metric) {
//        return (Distance)this.convertAndReturn(this.delegate.geoDist(key, member1, member2, metric), this.identityConverter);
//    }
//
//    public Distance geoDist(String key, String member1, String member2, Metric metric) {
//        return this.geoDist(this.serialize(key), this.serialize(member1), this.serialize(member2), metric);
//    }
//
//    public List<String> geoHash(byte[] key, byte[]... members) {
//        return (List)this.convertAndReturn(this.delegate.geoHash(key, members), this.identityConverter);
//    }
//
//    public List<String> geoHash(String key, String... members) {
//        return (List)this.convertAndReturn(this.delegate.geoHash(this.serialize(key), this.serializeMulti(members)), this.identityConverter);
//    }
//
//    public List<Point> geoPos(byte[] key, byte[]... members) {
//        return (List)this.convertAndReturn(this.delegate.geoPos(key, members), this.identityConverter);
//    }
//
//    public List<Point> geoPos(String key, String... members) {
//        return this.geoPos(this.serialize(key), this.serializeMulti(members));
//    }
//
//    public GeoResults<GeoLocation<String>> geoRadius(String key, Circle within) {
//        return (GeoResults)this.convertAndReturn(this.delegate.geoRadius(this.serialize(key), within), this.byteGeoResultsToStringGeoResults);
//    }
//
//    public GeoResults<GeoLocation<String>> geoRadius(String key, Circle within, GeoRadiusCommandArgs args) {
//        return (GeoResults)this.convertAndReturn(this.delegate.geoRadius(this.serialize(key), within, args), this.byteGeoResultsToStringGeoResults);
//    }
//
//    public GeoResults<GeoLocation<String>> geoRadiusByMember(String key, String member, double radius) {
//        return this.geoRadiusByMember(key, member, new Distance(radius, DistanceUnit.METERS));
//    }
//
//    public GeoResults<GeoLocation<String>> geoRadiusByMember(String key, String member, Distance radius) {
//        return (GeoResults)this.convertAndReturn(this.delegate.geoRadiusByMember(this.serialize(key), this.serialize(member), radius), this.byteGeoResultsToStringGeoResults);
//    }
//
//    public GeoResults<GeoLocation<String>> geoRadiusByMember(String key, String member, Distance radius, GeoRadiusCommandArgs args) {
//        return (GeoResults)this.convertAndReturn(this.delegate.geoRadiusByMember(this.serialize(key), this.serialize(member), radius, args), this.byteGeoResultsToStringGeoResults);
//    }
//
//    public GeoResults<GeoLocation<byte[]>> geoRadius(byte[] key, Circle within) {
//        return (GeoResults)this.convertAndReturn(this.delegate.geoRadius(key, within), this.identityConverter);
//    }
//
//    public GeoResults<GeoLocation<byte[]>> geoRadius(byte[] key, Circle within, GeoRadiusCommandArgs args) {
//        return (GeoResults)this.convertAndReturn(this.delegate.geoRadius(key, within, args), this.identityConverter);
//    }
//
//    public GeoResults<GeoLocation<byte[]>> geoRadiusByMember(byte[] key, byte[] member, double radius) {
//        return this.geoRadiusByMember(key, member, new Distance(radius, DistanceUnit.METERS));
//    }
//
//    public GeoResults<GeoLocation<byte[]>> geoRadiusByMember(byte[] key, byte[] member, Distance radius) {
//        return (GeoResults)this.convertAndReturn(this.delegate.geoRadiusByMember(key, member, radius), this.identityConverter);
//    }
//
//    public GeoResults<GeoLocation<byte[]>> geoRadiusByMember(byte[] key, byte[] member, Distance radius, GeoRadiusCommandArgs args) {
//        return (GeoResults)this.convertAndReturn(this.delegate.geoRadiusByMember(key, member, radius, args), this.identityConverter);
//    }
//
//    public Long geoRemove(byte[] key, byte[]... members) {
//        return this.zRem(key, members);
//    }
//
//    public Long geoRemove(String key, String... members) {
//        return this.geoRemove(this.serialize(key), this.serializeMulti(members));
//    }

    @Override
    public List<Object> closePipeline() {
        List var1;
        try {
            var1 = this.convertResults(this.delegate.closePipeline(), this.pipelineConverters);
        } finally {
            this.pipelineConverters.clear();
        }

        return var1;
    }

    @Override
    public boolean isPipelined() {
        return this.delegate.isPipelined();
    }

    @Override
    public void openPipeline() {
        this.delegate.openPipeline();
    }

    @Override
    public Object execute(String command) {
        return this.execute(command, EMPTY_2D_BYTE_ARRAY);
    }

    @Override
    public Object execute(String command, byte[]... args) {
        return this.convertAndReturn(this.delegate.execute(command, args), this.identityConverter);
    }

    @Override
    public Object execute(String command, String... args) {
        return this.execute(command, this.serializeMulti(args));
    }

    @Override
    public Boolean pExpire(String key, long millis) {
        return this.pExpire(this.serialize(key), millis);
    }

    @Override
    public Boolean pExpireAt(String key, long unixTimeInMillis) {
        return this.pExpireAt(this.serialize(key), unixTimeInMillis);
    }

    @Override
    public Long pTtl(String key) {
        return this.pTtl(this.serialize(key));
    }

    @Override
    public Long pTtl(String key, TimeUnit timeUnit) {
        return this.pTtl(this.serialize(key), timeUnit);
    }

    @Override
    public String scriptLoad(String script) {
        return this.scriptLoad(this.serialize(script));
    }

    public <T> T eval(String script, ReturnType returnType, int numKeys, String... keysAndArgs) {
        return this.eval(this.serialize(script), returnType, numKeys, this.serializeMulti(keysAndArgs));
    }

    public <T> T evalSha(String scriptSha1, ReturnType returnType, int numKeys, String... keysAndArgs) {
        return this.evalSha(scriptSha1, returnType, numKeys, this.serializeMulti(keysAndArgs));
    }

    @Override
    public Long time() {
        return (Long)this.convertAndReturn(this.delegate.time(), this.identityConverter);
    }

    @Override
    public List<RedisClientInfo> getClientList() {
        return (List)this.convertAndReturn(this.delegate.getClientList(), this.identityConverter);
    }

    @Override
    public void slaveOf(String host, int port) {
        this.delegate.slaveOf(host, port);
    }

    @Override
    public void slaveOfNoOne() {
        this.delegate.slaveOfNoOne();
    }

    public Cursor<byte[]> scan(ScanOptions options) {
        return this.delegate.scan(options);
    }

    public Cursor<Tuple> zScan(byte[] key, ScanOptions options) {
        return this.delegate.zScan(key, options);
    }

    public Cursor<byte[]> sScan(byte[] key, ScanOptions options) {
        return this.delegate.sScan(key, options);
    }

    public Cursor<Map.Entry<byte[], byte[]>> hScan(byte[] key, ScanOptions options) {
        return this.delegate.hScan(key, options);
    }

    @Override
    @Nullable
    public Long hStrLen(byte[] key, byte[] field) {
        return (Long)this.convertAndReturn(this.delegate.hStrLen(key, field), this.identityConverter);
    }

    @Override
    public void setClientName(byte[] name) {
        this.delegate.setClientName(name);
    }

    @Override
    public void setClientName(String name) {
        this.setClientName(this.serializer.serialize(name));
    }

    @Override
    public void killClient(String host, int port) {
        this.delegate.killClient(host, port);
    }

    @Override
    public String getClientName() {
        return (String)this.convertAndReturn(this.delegate.getClientName(), this.identityConverter);
    }

    public Cursor<Map.Entry<String, String>> hScan(String key, ScanOptions options) {
        return new ConvertingCursor(this.delegate.hScan(this.serialize(key), options), (source) -> {
            return new Map.Entry<String, String>() {

                @Override
                public String getKey() {
                    return (String)DefaultStringRedisConnection.this.bytesToString.convert(source.getKey());
                }

                @Override
                public String getValue() {
                    return (String)DefaultStringRedisConnection.this.bytesToString.convert(source.getValue());
                }

                @Override
                public String setValue(String value) {
                    throw new UnsupportedOperationException("Cannot set value for entry in cursor");
                }
            };
        });
    }

    @Override
    public Long hStrLen(String key, String field) {
        return this.hStrLen(this.serialize(key), this.serialize(field));
    }

    public Cursor<String> sScan(String key, ScanOptions options) {
        return new ConvertingCursor(this.delegate.sScan(this.serialize(key), options), this.bytesToString);
    }

    public Cursor<StringTuple> zScan(String key, ScanOptions options) {
        return new ConvertingCursor(this.delegate.zScan(this.serialize(key), options), new DefaultStringRedisConnection.TupleConverter());
    }

    @Override
    public RedisSentinelConnection getSentinelConnection() {
        return this.delegate.getSentinelConnection();
    }

    @Override
    public Set<String> zRangeByScore(String key, String min, String max) {
        return (Set)this.convertAndReturn(this.delegate.zRangeByScore(this.serialize(key), min, max), this.byteSetToStringSet);
    }

    @Override
    public Set<String> zRangeByScore(String key, String min, String max, long offset, long count) {
        return (Set)this.convertAndReturn(this.delegate.zRangeByScore(this.serialize(key), min, max, offset, count), this.byteSetToStringSet);
    }

    @Override
    public Set<byte[]> zRangeByScore(byte[] key, String min, String max) {
        return (Set)this.convertAndReturn(this.delegate.zRangeByScore(key, min, max), this.identityConverter);
    }

    @Override
    public Set<byte[]> zRangeByScore(byte[] key, String min, String max, long offset, long count) {
        return (Set)this.convertAndReturn(this.delegate.zRangeByScore(key, min, max, offset, count), this.identityConverter);
    }

    @Override
    public Long pfAdd(byte[] key, byte[]... values) {
        return (Long)this.convertAndReturn(this.delegate.pfAdd(key, values), this.identityConverter);
    }

    @Override
    public Long pfAdd(String key, String... values) {
        return this.pfAdd(this.serialize(key), this.serializeMulti(values));
    }

    @Override
    public Long pfCount(byte[]... keys) {
        return (Long)this.convertAndReturn(this.delegate.pfCount(keys), this.identityConverter);
    }

    @Override
    public Long pfCount(String... keys) {
        return this.pfCount(this.serializeMulti(keys));
    }

    @Override
    public void pfMerge(byte[] destinationKey, byte[]... sourceKeys) {
        this.delegate.pfMerge(destinationKey, sourceKeys);
    }

    @Override
    public void pfMerge(String destinationKey, String... sourceKeys) {
        this.pfMerge(this.serialize(destinationKey), this.serializeMulti(sourceKeys));
    }

    @Override
    public Set<byte[]> zRangeByLex(byte[] key) {
        return (Set)this.convertAndReturn(this.delegate.zRangeByLex(key), this.identityConverter);
    }

    public Set<byte[]> zRangeByLex(byte[] key, RedisZSetCommands.Range range) {
        return (Set)this.convertAndReturn(this.delegate.zRangeByLex(key, range), this.identityConverter);
    }

    public Set<byte[]> zRangeByLex(byte[] key, RedisZSetCommands.Range range, Limit limit) {
        return (Set)this.convertAndReturn(this.delegate.zRangeByLex(key, range, limit), this.identityConverter);
    }

    @Override
    public Set<String> zRangeByLex(String key) {
        return this.zRangeByLex(key, RedisZSetCommands.Range.unbounded());
    }

    public Set<String> zRangeByLex(String key, RedisZSetCommands.Range range) {
        return this.zRangeByLex((String)key, range, (Limit)null);
    }

    public Set<String> zRangeByLex(String key, RedisZSetCommands.Range range, Limit limit) {
        return (Set)this.convertAndReturn(this.delegate.zRangeByLex(this.serialize(key), range), this.byteSetToStringSet);
    }

    @Override
    public void migrate(byte[] key, RedisNode target, int dbIndex, @Nullable MigrateOption option) {
        this.delegate.migrate(key, target, dbIndex, option);
    }

    @Override
    public void migrate(byte[] key, RedisNode target, int dbIndex, @Nullable MigrateOption option, long timeout) {
        this.delegate.migrate(key, target, dbIndex, option, timeout);
    }

    public void setDeserializePipelineAndTxResults(boolean deserializePipelineAndTxResults) {
        this.deserializePipelineAndTxResults = deserializePipelineAndTxResults;
    }

    @Nullable
    private <T> T convertAndReturn(@Nullable Object value, Converter converter) {
        if (this.isFutureConversion()) {
            this.addResultConverter(converter);
            return null;
        } else {
            return value == null ? null : (T) (ObjectUtils.nullSafeEquals(converter, this.identityConverter) ? value : converter.convert(value));
        }
    }

    private void addResultConverter(Converter<?, ?> converter) {
        if (this.isQueueing()) {
            this.txConverters.add(converter);
        } else {
            this.pipelineConverters.add(converter);
        }

    }

    private boolean isFutureConversion() {
        return this.isPipelined() || this.isQueueing();
    }

    private List<Object> convertResults(@Nullable List<Object> results, Queue<Converter> converters) {
        if (this.deserializePipelineAndTxResults && results != null) {
            if (results.size() != converters.size()) {
                this.log.warn("Delegate returned an unexpected number of results. Abandoning type conversion.");
                return results;
            } else {
                List<Object> convertedResults = new ArrayList(results.size());
                Iterator var4 = results.iterator();

                while(var4.hasNext()) {
                    Object result = var4.next();
                    Converter converter = (Converter)converters.remove();
                    convertedResults.add(result == null ? null : converter.convert(result));
                }

                return convertedResults;
            }
        } else {
            return results;
        }
    }

    @Override
    public List<Long> bitField(byte[] key, BitFieldSubCommands subCommands) {
        return this.delegate.bitField(key, subCommands);
    }

    @Override
    public List<Long> bitfield(String key, BitFieldSubCommands operation) {
        List<Long> results = this.delegate.bitField(this.serialize(key), operation);
        if (this.isFutureConversion()) {
            this.addResultConverter(this.identityConverter);
        }

        return results;
    }

    @Override
    public RedisConnection getDelegate() {
        return this.delegate;
    }

    private class TransactionResultConverter implements Converter<List<Object>, List<Object>> {
        private Queue<Converter> txConverters;

        public TransactionResultConverter(Queue<Converter> txConverters) {
            this.txConverters = txConverters;
        }

        @Override
        public List<Object> convert(List<Object> execResults) {
            return DefaultStringRedisConnection.this.convertResults(execResults, this.txConverters);
        }
    }

    private class IdentityConverter<S, T> implements Converter<S, T> {
        private IdentityConverter() {
        }

        @Override
        public Object convert(Object source) {
            return source;
        }
    }

    private class StringTupleConverter implements Converter<StringTuple, Tuple> {
        private StringTupleConverter() {
        }

        @Override
        public Tuple convert(StringTuple source) {
            return new DefaultTuple(source.getValue(), source.getScore());
        }
    }

    private class TupleConverter implements Converter<Tuple, StringTuple> {
        private TupleConverter() {
        }

        @Override
        public StringTuple convert(Tuple source) {
            return new DefaultStringTuple(source, (String)DefaultStringRedisConnection.this.serializer.deserialize(source.getValue()));
        }
    }

    private class DeserializingConverter implements Converter<byte[], String> {
        private DeserializingConverter() {
        }

        @Override
        public String convert(byte[] source) {
            return (String)DefaultStringRedisConnection.this.serializer.deserialize(source);
        }
    }

}
