package cn.choleece.base.framework.template;

import cn.choleece.base.framework.exception.InvalidDataAccessApiUsageException;
import cn.choleece.base.framework.redis.connection.*;
import cn.choleece.base.framework.redis.core.*;
import cn.choleece.base.framework.redis.core.query.QueryUtils;
import cn.choleece.base.framework.redis.core.query.SortQuery;
import cn.choleece.base.framework.redis.core.types.RedisClientInfo;
import cn.choleece.base.framework.redis.serializer.JdkSerializationRedisSerializer;
import cn.choleece.base.framework.redis.serializer.RedisSerializer;
import cn.choleece.base.framework.redis.serializer.SerializationUtils;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.lang.Nullable;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import java.io.Closeable;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-15 21:25
 **/
public class CusRedisTemplate<K, V> extends RedisAccessor implements RedisOperations<K, V>, BeanClassLoaderAware {

    /**
     * 是否开启事物支持
     */
    private boolean enableTransactionSupport = false;

    /**
     * 暴露连接
     */
    private boolean exposeConnection = false;

    /**
     * 是否已初始化
     */
    private boolean initialized = false;

    /**
     * 是否开启默认的序列化
     */
    private boolean enableDefaultSerializer = true;

    /**
     * 默认序列化的方式
     */
    @Nullable
    private RedisSerializer<?> defaultSerializer;

    @Nullable
    private ClassLoader classLoader;

    /**
     * redis 非hash key 的序列化方式
     */
    @Nullable
    private RedisSerializer keySerializer = null;

    /**
     * redis value 的序列化方式
     */
    @Nullable
    private RedisSerializer valueSerializer = null;

    /**
     * redis hash key 的序列化方式
     */
    @Nullable
    private RedisSerializer hashKeySerializer = null;

    /**
     * redis hash value 的序列化方式
     */
    @Nullable
    private RedisSerializer hashValueSerializer = null;

    /**
     * redis string 的序列化方式
     */
    private RedisSerializer<String> stringSerializer = RedisSerializer.string();

    /**
     * 脚本执行器 暂时先不考虑
     */
//    @Nullable
//    private ScriptExecutor<K> scriptExecutor;

    /**
     * string value 操作
     */
    @Nullable
    private ValueOperations<K, V> valueOps;

    // 以下为非string 类型操作
//    @Nullable
//    private ListOperations<K, V> listOps;
//    @Nullable
//    private SetOperations<K, V> setOps;
//    @Nullable
//    private ZSetOperations<K, V> zSetOps;
//    @Nullable
//    private GeoOperations<K, V> geoOps;
//    @Nullable
//    private HyperLogLogOperations<K, V> hllOps;

    public CusRedisTemplate() {
    }

    /**
     * 此方法将在所有的属性设置完成后进行调用
     * 参考：https://www.cnblogs.com/feiyun126/p/7685312.html
     */
    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        boolean defaultUsed = false;

        // 属性加载完成后进行默认设置
        if (this.defaultSerializer == null) {
            this.defaultSerializer = new JdkSerializationRedisSerializer(this.classLoader != null ? this.classLoader : this.getClass().getClassLoader());
        }

        if (this.enableDefaultSerializer) {
            if (this.keySerializer == null) {
                this.keySerializer = this.defaultSerializer;
                defaultUsed = true;
            }

            if (this.valueSerializer == null) {
                this.valueSerializer = this.defaultSerializer;
                defaultUsed = true;
            }

            if (this.hashKeySerializer == null) {
                this.hashKeySerializer = this.defaultSerializer;
                defaultUsed = true;
            }

            if (this.hashValueSerializer == null) {
                this.hashValueSerializer = this.defaultSerializer;
                defaultUsed = true;
            }
        }

        if (this.enableDefaultSerializer && defaultUsed) {
            Assert.notNull(this.defaultSerializer, "default serializer null and not all serializers initialized");
        }

//        if (this.scriptExecutor == null) {
//            this.scriptExecutor = new DefaultScriptExecutor(this);
//        }

        this.initialized = true;
    }

    @Override
    @Nullable
    public <T> T execute(RedisCallback<T> action) {
        return execute(action, isExposeConnection());
    }

    @Nullable
    public <T> T execute(RedisCallback<T> action, boolean exposeConnection) {
        return execute(action, exposeConnection, false);
    }

    @Nullable
    public <T> T execute(RedisCallback<T> action, boolean exposeConnection, boolean pipeline) {
        Assert.isTrue(initialized, "template not initialized; call afterPropertiesSet() before using it");
        Assert.notNull(action, "Callback object must not be null");

        RedisConnectionFactory factory = getRequiredConnectionFactory();
        RedisConnection conn = null;
        try {

            if (enableTransactionSupport) {
                // only bind resources in case of potential transaction synchronization
                conn = RedisConnectionUtils.bindConnection(factory, enableTransactionSupport);
            } else {
                conn = RedisConnectionUtils.getConnection(factory);
            }

            boolean existingConnection = TransactionSynchronizationManager.hasResource(factory);

            RedisConnection connToUse = preProcessConnection(conn, existingConnection);

            boolean pipelineStatus = connToUse.isPipelined();
            if (pipeline && !pipelineStatus) {
                connToUse.openPipeline();
            }

            RedisConnection connToExpose = (exposeConnection ? connToUse : createRedisConnectionProxy(connToUse));
            T result = action.doInRedis(connToExpose);

            // close pipeline
            if (pipeline && !pipelineStatus) {
                connToUse.closePipeline();
            }

            // TODO: any other connection processing?
            return postProcessResult(result, connToUse, existingConnection);
        } finally {
            RedisConnectionUtils.releaseConnection(conn, factory, enableTransactionSupport);
        }
    }

    @Override
    public <T> T execute(SessionCallback<T> session) {
        Assert.isTrue(initialized, "template not initialized; call afterPropertiesSet() before using it");
        Assert.notNull(session, "Callback object must not be null");

        RedisConnectionFactory factory = getRequiredConnectionFactory();
        // bind connection
        RedisConnectionUtils.bindConnection(factory, enableTransactionSupport);
        try {
            return session.execute(this);
        } finally {
            RedisConnectionUtils.unbindConnection(factory);
        }
    }

    @Override
    public List<Object> executePipelined(SessionCallback<?> session) {
        return executePipelined(session, valueSerializer);
    }

    @Override
    public List<Object> executePipelined(SessionCallback<?> session, @Nullable RedisSerializer<?> resultSerializer) {
        Assert.isTrue(initialized, "template not initialized; call afterPropertiesSet() before using it");
        Assert.notNull(session, "Callback object must not be null");

        RedisConnectionFactory factory = getRequiredConnectionFactory();
        // bind connection
        RedisConnectionUtils.bindConnection(factory, enableTransactionSupport);
        try {
            return execute((RedisCallback<List<Object>>) connection -> {
                connection.openPipeline();
                boolean pipelinedClosed = false;
                try {
                    Object result = executeSession(session);
                    if (result != null) {
                        throw new org.springframework.dao.InvalidDataAccessApiUsageException(
                                "Callback cannot return a non-null value as it gets overwritten by the pipeline");
                    }
                    List<Object> closePipeline = connection.closePipeline();
                    pipelinedClosed = true;
                    return deserializeMixedResults(closePipeline, resultSerializer, hashKeySerializer, hashValueSerializer);
                } finally {
                    if (!pipelinedClosed) {
                        connection.closePipeline();
                    }
                }
            });
        } finally {
            RedisConnectionUtils.unbindConnection(factory);
        }
    }

    @Override
    public List<Object> executePipelined(RedisCallback<?> action) {
        return executePipelined(action, valueSerializer);
    }

    @Override
    public List<Object> executePipelined(RedisCallback<?> action, @Nullable RedisSerializer<?> resultSerializer) {
        return execute((RedisCallback<List<Object>>) connection -> {
            connection.openPipeline();
            boolean pipelinedClosed = false;
            try {
                Object result = action.doInRedis(connection);
                if (result != null) {
                    throw new InvalidDataAccessApiUsageException(
                            "Callback cannot return a non-null value as it gets overwritten by the pipeline");
                }
                List<Object> closePipeline = connection.closePipeline();
                pipelinedClosed = true;
                return deserializeMixedResults(closePipeline, resultSerializer, hashKeySerializer, hashValueSerializer);
            } finally {
                if (!pipelinedClosed) {
                    connection.closePipeline();
                }
            }
        });
    }

//    public <T> T execute(RedisScript<T> script, List<K> keys, Object... args) {
//        return this.scriptExecutor.execute(script, keys, args);
//    }
//
//    public <T> T execute(RedisScript<T> script, RedisSerializer<?> argsSerializer, RedisSerializer<T> resultSerializer, List<K> keys, Object... args) {
//        return this.scriptExecutor.execute(script, argsSerializer, resultSerializer, keys, args);
//    }

    @Override
    @Nullable
    public <T extends Closeable> T executeWithStickyConnection(RedisCallback<T> callback) {
        Assert.isTrue(initialized, "template not initialized; call afterPropertiesSet() before using it");
        Assert.notNull(callback, "Callback object must not be null");

        RedisConnectionFactory factory = getRequiredConnectionFactory();

        RedisConnection connection = preProcessConnection(RedisConnectionUtils.doGetConnection(factory, true, false, false),
                false);

        return callback.doInRedis(connection);
    }

    private Object executeSession(SessionCallback<?> session) {
        return session.execute(this);
    }

    protected RedisConnection createRedisConnectionProxy(RedisConnection pm) {
        Class<?>[] ifcs = ClassUtils.getAllInterfacesForClass(pm.getClass(), getClass().getClassLoader());
        return (RedisConnection) Proxy.newProxyInstance(pm.getClass().getClassLoader(), ifcs,
                new CloseSuppressingInvocationHandler(pm));
    }

    protected RedisConnection preProcessConnection(RedisConnection connection, boolean existingConnection) {
        return connection;
    }

    @Nullable
    protected <T> T postProcessResult(@Nullable T result, RedisConnection conn, boolean existingConnection) {
        return result;
    }

    public boolean isExposeConnection() {
        return this.exposeConnection;
    }

    public void setExposeConnection(boolean exposeConnection) {
        this.exposeConnection = exposeConnection;
    }

    public boolean isEnableDefaultSerializer() {
        return this.enableDefaultSerializer;
    }

    public void setEnableDefaultSerializer(boolean enableDefaultSerializer) {
        this.enableDefaultSerializer = enableDefaultSerializer;
    }

    @Nullable
    public RedisSerializer<?> getDefaultSerializer() {
        return this.defaultSerializer;
    }

    public void setDefaultSerializer(RedisSerializer<?> serializer) {
        this.defaultSerializer = serializer;
    }

    public void setKeySerializer(RedisSerializer<?> serializer) {
        this.keySerializer = serializer;
    }

    @Override
    public RedisSerializer<?> getKeySerializer() {
        return this.keySerializer;
    }

    public void setValueSerializer(RedisSerializer<?> serializer) {
        this.valueSerializer = serializer;
    }

    public RedisSerializer<?> getValueSerializer() {
        return this.valueSerializer;
    }

    @Override
    public RedisSerializer<?> getHashKeySerializer() {
        return this.hashKeySerializer;
    }

    public void setHashKeySerializer(RedisSerializer<?> hashKeySerializer) {
        this.hashKeySerializer = hashKeySerializer;
    }

    @Override
    public RedisSerializer<?> getHashValueSerializer() {
        return this.hashValueSerializer;
    }

    public void setHashValueSerializer(RedisSerializer<?> hashValueSerializer) {
        this.hashValueSerializer = hashValueSerializer;
    }

    public RedisSerializer<String> getStringSerializer() {
        return this.stringSerializer;
    }

    public void setStringSerializer(RedisSerializer<String> stringSerializer) {
        this.stringSerializer = stringSerializer;
    }

//    public void setScriptExecutor(ScriptExecutor<K> scriptExecutor) {
//        this.scriptExecutor = scriptExecutor;
//    }

    private byte[] rawKey(Object key) {
        Assert.notNull(key, "non null key required");
        if (keySerializer == null && key instanceof byte[]) {
            return (byte[]) key;
        }
        return keySerializer.serialize(key);

    }

    private byte[] rawString(String key) {
        return stringSerializer.serialize(key);
    }

    private byte[] rawValue(Object value) {
        if (valueSerializer == null && value instanceof byte[]) {
            return (byte[]) value;
        }
        return valueSerializer.serialize(value);
    }

    private byte[][] rawKeys(Collection<K> keys) {
        final byte[][] rawKeys = new byte[keys.size()][];

        int i = 0;
        for (K key : keys) {
            rawKeys[i++] = rawKey(key);
        }

        return rawKeys;
    }

    @SuppressWarnings("unchecked")
    private K deserializeKey(byte[] value) {
        return keySerializer != null ? (K) keySerializer.deserialize(value) : (K) value;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Nullable
    private List<Object> deserializeMixedResults(@Nullable List<Object> rawValues,
                                                 @Nullable RedisSerializer valueSerializer, @Nullable RedisSerializer hashKeySerializer,
                                                 @Nullable RedisSerializer hashValueSerializer) {

        if (rawValues == null) {
            return null;
        }

        List<Object> values = new ArrayList<>();
        for (Object rawValue : rawValues) {

            if (rawValue instanceof byte[] && valueSerializer != null) {
                values.add(valueSerializer.deserialize((byte[]) rawValue));
            } else if (rawValue instanceof List) {
                // Lists are the only potential Collections of mixed values....
                values.add(deserializeMixedResults((List) rawValue, valueSerializer, hashKeySerializer, hashValueSerializer));
            } else if (rawValue instanceof Set && !(((Set) rawValue).isEmpty())) {
                values.add(deserializeSet((Set) rawValue, valueSerializer));
            } else if (rawValue instanceof Map && !(((Map) rawValue).isEmpty())
                    && ((Map) rawValue).values().iterator().next() instanceof byte[]) {
                values.add(SerializationUtils.deserialize((Map) rawValue, hashKeySerializer, hashValueSerializer));
            } else {
                values.add(rawValue);
            }
        }

        return values;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Set<?> deserializeSet(Set rawSet, @Nullable RedisSerializer valueSerializer) {

        if (rawSet.isEmpty()) {
            return rawSet;
        }

        Object setValue = rawSet.iterator().next();

        if (setValue instanceof byte[] && valueSerializer != null) {
            return (SerializationUtils.deserialize(rawSet, valueSerializer));
        } else if (setValue instanceof RedisZSetCommands.Tuple) {
            return convertTupleValues(rawSet, valueSerializer);
        } else {
            return rawSet;
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Set<ZSetOperations.TypedTuple<V>> convertTupleValues(Set<RedisZSetCommands.Tuple> rawValues, @Nullable RedisSerializer valueSerializer) {

        Set<ZSetOperations.TypedTuple<V>> set = new LinkedHashSet<>(rawValues.size());
        for (RedisZSetCommands.Tuple rawValue : rawValues) {
            Object value = rawValue.getValue();
            if (valueSerializer != null) {
                value = valueSerializer.deserialize(rawValue.getValue());
            }
            set.add(new DefaultTypedTuple(value, rawValue.getScore()));
        }
        return set;
    }

    @Override
    public Boolean delete(K key) {

        byte[] rawKey = rawKey(key);

        Long result = execute(connection -> connection.del(rawKey), true);

        return result != null && result.intValue() == 1;
    }

    @Override
    public Long delete(Collection<K> keys) {

        if (CollectionUtils.isEmpty(keys)) {
            return 0L;
        }

        byte[][] rawKeys = rawKeys(keys);

        return execute(connection -> connection.del(rawKeys), true);
    }

    @Override
    public Boolean unlink(K key) {

        byte[] rawKey = rawKey(key);

        Long result = execute(connection -> connection.unlink(rawKey), true);

        return result != null && result.intValue() == 1;
    }

    @Override
    public Long unlink(Collection<K> keys) {

        if (CollectionUtils.isEmpty(keys)) {
            return 0L;
        }

        byte[][] rawKeys = rawKeys(keys);

        return execute(connection -> connection.unlink(rawKeys), true);
    }

    @Override
    public Boolean hasKey(K key) {

        byte[] rawKey = rawKey(key);

        return execute(connection -> connection.exists(rawKey), true);
    }

    @Override
    public Long countExistingKeys(Collection<K> keys) {

        Assert.notNull(keys, "Keys must not be null!");

        byte[][] rawKeys = rawKeys(keys);
        return execute(connection -> connection.exists(rawKeys), true);
    }

    @Override
    public Boolean expire(K key, final long timeout, final TimeUnit unit) {

        byte[] rawKey = rawKey(key);
        long rawTimeout = TimeoutUtils.toMillis(timeout, unit);

        return execute(connection -> {
            try {
                return connection.pExpire(rawKey, rawTimeout);
            } catch (Exception e) {
                // Driver may not support pExpire or we may be running on Redis 2.4
                return connection.expire(rawKey, TimeoutUtils.toSeconds(timeout, unit));
            }
        }, true);
    }

    @Override
    public Boolean expireAt(K key, final Date date) {

        byte[] rawKey = rawKey(key);

        return execute(connection -> {
            try {
                return connection.pExpireAt(rawKey, date.getTime());
            } catch (Exception e) {
                return connection.expireAt(rawKey, date.getTime() / 1000);
            }
        }, true);
    }

    @Override
    public void convertAndSend(String channel, Object message) {

        Assert.hasText(channel, "a non-empty channel is required");

        byte[] rawChannel = rawString(channel);
        byte[] rawMessage = rawValue(message);

        execute(connection -> {
            connection.publish(rawChannel, rawMessage);
            return null;
        }, true);
    }

    @Override
    public Long getExpire(K key) {

        byte[] rawKey = rawKey(key);
        return execute(connection -> connection.ttl(rawKey), true);
    }

    @Override
    public Long getExpire(K key, final TimeUnit timeUnit) {

        byte[] rawKey = rawKey(key);
        return execute(connection -> {
            try {
                return connection.pTtl(rawKey, timeUnit);
            } catch (Exception e) {
                // Driver may not support pTtl or we may be running on Redis 2.4
                return connection.ttl(rawKey, timeUnit);
            }
        }, true);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<K> keys(K pattern) {

        byte[] rawKey = rawKey(pattern);
        Set<byte[]> rawKeys = execute(connection -> connection.keys(rawKey), true);

        return keySerializer != null ? SerializationUtils.deserialize(rawKeys, keySerializer) : (Set<K>) rawKeys;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.core.RedisOperations#persist(java.lang.Object)
     */
    @Override
    public Boolean persist(K key) {

        byte[] rawKey = rawKey(key);
        return execute(connection -> connection.persist(rawKey), true);
    }

    @Override
    public Boolean move(K key, final int dbIndex) {

        byte[] rawKey = rawKey(key);
        return execute(connection -> connection.move(rawKey, dbIndex), true);
    }

    @Override
    public K randomKey() {

        byte[] rawKey = execute(RedisKeyCommands::randomKey, true);
        return deserializeKey(rawKey);
    }

    @Override
    public void rename(K oldKey, K newKey) {

        byte[] rawOldKey = rawKey(oldKey);
        byte[] rawNewKey = rawKey(newKey);

        execute(connection -> {
            connection.rename(rawOldKey, rawNewKey);
            return null;
        }, true);
    }

    @Override
    public Boolean renameIfAbsent(K oldKey, K newKey) {

        byte[] rawOldKey = rawKey(oldKey);
        byte[] rawNewKey = rawKey(newKey);
        return execute(connection -> connection.renameNX(rawOldKey, rawNewKey), true);
    }

    @Override
    public DataType type(K key) {

        byte[] rawKey = rawKey(key);
        return execute(connection -> connection.type(rawKey), true);
    }

    @Override
    public byte[] dump(K key) {

        byte[] rawKey = rawKey(key);
        return execute(connection -> connection.dump(rawKey), true);
    }

    @Override
    public void restore(K key, final byte[] value, long timeToLive, TimeUnit unit, boolean replace) {

        byte[] rawKey = rawKey(key);
        long rawTimeout = TimeoutUtils.toMillis(timeToLive, unit);

        execute(connection -> {
            connection.restore(rawKey, rawTimeout, value, replace);
            return null;
        }, true);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<V> sort(SortQuery<K> query) {
        return sort(query, valueSerializer);
    }

    @Override
    public <T> List<T> sort(SortQuery<K> query, @Nullable RedisSerializer<T> resultSerializer) {

        byte[] rawKey = rawKey(query.getKey());
        SortParameters params = QueryUtils.convertQuery(query, stringSerializer);

        List<byte[]> vals = execute(connection -> connection.sort(rawKey, params), true);

        return SerializationUtils.deserialize(vals, resultSerializer);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> sort(SortQuery<K> query, BulkMapper<T, V> bulkMapper) {
        return sort(query, bulkMapper, valueSerializer);
    }

    @Override
    public <T, S> List<T> sort(SortQuery<K> query, BulkMapper<T, S> bulkMapper,
                               @Nullable RedisSerializer<S> resultSerializer) {

        List<S> values = sort(query, resultSerializer);

        if (values == null || values.isEmpty()) {
            return Collections.emptyList();
        }

        int bulkSize = query.getGetPattern().size();
        List<T> result = new ArrayList<>(values.size() / bulkSize + 1);

        List<S> bulk = new ArrayList<>(bulkSize);
        for (S s : values) {

            bulk.add(s);
            if (bulk.size() == bulkSize) {
                result.add(bulkMapper.mapBulk(Collections.unmodifiableList(bulk)));
                // create a new list (we could reuse the old one but the client might hang on to it for some reason)
                bulk = new ArrayList<>(bulkSize);
            }
        }

        return result;
    }

    @Override
    public Long sort(SortQuery<K> query, K storeKey) {

        byte[] rawStoreKey = rawKey(storeKey);
        byte[] rawKey = rawKey(query.getKey());
        SortParameters params = QueryUtils.convertQuery(query, stringSerializer);

        return execute(connection -> connection.sort(rawKey, params, rawStoreKey), true);
    }

    @Override
    public void killClient(final String host, final int port) {

        execute((RedisCallback<Void>) connection -> {
            connection.killClient(host, port);
            return null;
        });
    }

    @Override
    public List<RedisClientInfo> getClientList() {
        return execute(RedisServerCommands::getClientList);
    }

    @Override
    public void slaveOf(final String host, final int port) {

        execute((RedisCallback<Void>) connection -> {

            connection.slaveOf(host, port);
            return null;
        });
    }

    @Override
    public void slaveOfNoOne() {

        execute((RedisCallback<Void>) connection -> {
            connection.slaveOfNoOne();
            return null;
        });
    }

//    public ClusterOperations<K, V> opsForCluster() {
//        return new DefaultClusterOperations(this);
//    }
//
//    public GeoOperations<K, V> opsForGeo() {
//        if (this.geoOps == null) {
//            this.geoOps = new DefaultGeoOperations(this);
//        }
//
//        return this.geoOps;
//    }
//
//    public BoundGeoOperations<K, V> boundGeoOps(K key) {
//        return new DefaultBoundGeoOperations(key, this);
//    }
//
//    public <HK, HV> BoundHashOperations<K, HK, HV> boundHashOps(K key) {
//        return new DefaultBoundHashOperations(key, this);
//    }
//
//    public <HK, HV> HashOperations<K, HK, HV> opsForHash() {
//        return new DefaultHashOperations(this);
//    }
//
//    public HyperLogLogOperations<K, V> opsForHyperLogLog() {
//        if (this.hllOps == null) {
//            this.hllOps = new DefaultHyperLogLogOperations(this);
//        }
//
//        return this.hllOps;
//    }
//
//    public ListOperations<K, V> opsForList() {
//        if (this.listOps == null) {
//            this.listOps = new DefaultListOperations(this);
//        }
//
//        return this.listOps;
//    }
//
//    public BoundListOperations<K, V> boundListOps(K key) {
//        return new DefaultBoundListOperations(key, this);
//    }
//
//    public BoundSetOperations<K, V> boundSetOps(K key) {
//        return new DefaultBoundSetOperations(key, this);
//    }
//
//    public SetOperations<K, V> opsForSet() {
//        if (this.setOps == null) {
//            this.setOps = new DefaultSetOperations(this);
//        }
//
//        return this.setOps;
//    }
//
//    public BoundValueOperations<K, V> boundValueOps(K key) {
//        return new DefaultBoundValueOperations(key, this);
//    }
//
    @Override
    public ValueOperations<K, V> opsForValue() {

        if (valueOps == null) {
            valueOps = new DefaultValueOperations<>(this);
        }
        return valueOps;
    }
//
//    public BoundZSetOperations<K, V> boundZSetOps(K key) {
//        return new DefaultBoundZSetOperations(key, this);
//    }
//
//    public ZSetOperations<K, V> opsForZSet() {
//        if (this.zSetOps == null) {
//            this.zSetOps = new DefaultZSetOperations(this);
//        }
//
//        return this.zSetOps;
//    }

    public void setEnableTransactionSupport(boolean enableTransactionSupport) {
        this.enableTransactionSupport = enableTransactionSupport;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
}
