package cn.choleece.base.framework.redis.connection.jedis;

import cn.choleece.base.framework.exception.DataAccessException;
import cn.choleece.base.framework.redis.ExceptionTranslationStrategy;
import cn.choleece.base.framework.redis.FallbackExceptionTranslationStrategy;
import cn.choleece.base.framework.redis.RedisConnectionFailureException;
import cn.choleece.base.framework.redis.RedisSystemException;
import cn.choleece.base.framework.redis.connection.*;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import redis.clients.jedis.*;
import redis.clients.util.Pool;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @description: Jedis 链接
 * @author: sf
 * @time: 2019-10-14 15:25
 */
public class JedisConnection extends AbstractRedisConnection {

    private static final ExceptionTranslationStrategy EXCEPTION_TRANSLATION = new FallbackExceptionTranslationStrategy(JedisConverters.exceptionConverter());

    /**
     * jedis 实例
     */
    private final Jedis jedis;

    @Nullable
    private Transaction transaction;

    /**
     * Jedis 连接池
     */
    private final Pool<Jedis> pool;

    /**
     * 是否中断
     */
    private boolean broken;

    @Nullable
    private volatile JedisSubscription subscription;

    @Nullable
    private volatile Pipeline pipeline;

    /**
     * 与RedisProperties里的database一样
     */
    private final int dbIndex;

    private final String clientName;

    private boolean convertPipelineAndTxResults;

    private List<JedisResult> pipelinedResults;

    private Queue<FutureResult<Response<?>>> txResults;

    public JedisConnection(Jedis jedis) {
        this(jedis, (Pool)null, 0);
    }

    /**
     * Jedis 连接
     * @param jedis
     * @param pool
     * @param dbIndex
     */
    public JedisConnection(Jedis jedis, Pool<Jedis> pool, int dbIndex) {
        this(jedis, pool, dbIndex, (String)null);
    }

    protected JedisConnection(Jedis jedis, @Nullable Pool<Jedis> pool, int dbIndex, String clientName) {
        this.broken = false;
        this.convertPipelineAndTxResults = true;
        this.pipelinedResults = new ArrayList();
        this.txResults = new LinkedList();
        this.jedis = jedis;
        this.pool = pool;
        this.dbIndex = dbIndex;
        this.clientName = clientName;
        if ((long)dbIndex != jedis.getDB()) {
            try {
                this.select(dbIndex);
            } catch (DataAccessException var6) {
                this.close();
                throw var6;
            }
        }
    }

    protected DataAccessException convertJedisAccessException(Exception ex) {
        if (ex instanceof NullPointerException) {
            this.broken = true;
        }

        DataAccessException exception = EXCEPTION_TRANSLATION.translate(ex);
        if (exception instanceof RedisConnectionFailureException) {
            this.broken = true;
        }

        return (DataAccessException)(exception != null ? exception : new RedisSystemException(ex.getMessage(), ex));
    }

    public RedisKeyCommands keyCommands() {
        return new JedisKeyCommands(this);
    }

    public RedisStringCommands stringCommands() {
        return new JedisStringCommands(this);
    }

    public RedisListCommands listCommands() {
        return new JedisListCommands(this);
    }

    public RedisSetCommands setCommands() {
        return new JedisSetCommands(this);
    }

    public RedisZSetCommands zSetCommands() {
        return new JedisZSetCommands(this);
    }

    public RedisHashCommands hashCommands() {
        return new JedisHashCommands(this);
    }

    public RedisGeoCommands geoCommands() {
        return new JedisGeoCommands(this);
    }

    public RedisScriptingCommands scriptingCommands() {
        return new JedisScriptingCommands(this);
    }

    public RedisServerCommands serverCommands() {
        return new JedisServerCommands(this);
    }

    public RedisHyperLogLogCommands hyperLogLogCommands() {
        return new JedisHyperLogLogCommands(this);
    }

    public Object execute(String command, byte[]... args) {
        return this.execute(command, args, Connection::getOne, JedisClientUtils::getResponse);
    }

    <T> T execute(String command, byte[][] args, Function<Client, T> resultMapper, Function<Object, Response<?>> pipelineResponseMapper) {
        Assert.hasText(command, "A valid command needs to be specified!");
        Assert.notNull(args, "Arguments must not be null!");

        try {
            Client client = JedisClientUtils.sendCommand(command, args, this.jedis);
            if (!this.isQueueing() && !this.isPipelined()) {
                return resultMapper.apply(client);
            } else {
                Response<?> result = (Response)pipelineResponseMapper.apply(this.isPipelined() ? this.getRequiredPipeline() : this.getRequiredTransaction());
                if (this.isPipelined()) {
                    this.pipeline(this.newJedisResult(result));
                } else {
                    this.transaction(this.newJedisResult(result));
                }

                return null;
            }
        } catch (Exception var7) {
            throw this.convertJedisAccessException(var7);
        }
    }

    public void close() throws DataAccessException {
        super.close();
        if (this.pool != null) {
            if (this.broken) {
                this.pool.returnBrokenResource(this.jedis);
            } else {
                this.jedis.close();
            }

        } else {
            Exception exc = null;

            try {
                this.jedis.quit();
            } catch (Exception var4) {
                exc = var4;
            }

            try {
                this.jedis.disconnect();
            } catch (Exception var3) {
                exc = var3;
            }

            if (exc != null) {
                throw this.convertJedisAccessException(exc);
            }
        }
    }

    public Jedis getNativeConnection() {
        return this.jedis;
    }

    public boolean isClosed() {
        try {
            return !this.jedis.isConnected();
        } catch (Exception var2) {
            throw this.convertJedisAccessException(var2);
        }
    }

    public boolean isQueueing() {
        return JedisClientUtils.isInMulti(this.jedis);
    }

    public boolean isPipelined() {
        return this.pipeline != null;
    }

    public void openPipeline() {
        if (this.pipeline == null) {
            this.pipeline = this.jedis.pipelined();
        }

    }

    public List<Object> closePipeline() {
        if (this.pipeline != null) {
            List var1;
            try {
                var1 = this.convertPipelineResults();
            } finally {
                this.pipeline = null;
                this.pipelinedResults.clear();
            }

            return var1;
        } else {
            return Collections.emptyList();
        }
    }

    private List<Object> convertPipelineResults() {
        List<Object> results = new ArrayList();
        this.getRequiredPipeline().sync();
        Exception cause = null;
        Iterator var3 = this.pipelinedResults.iterator();

        while(var3.hasNext()) {
            JedisResult result = (JedisResult)var3.next();

            try {
                Object data = result.get();
                if (!result.isStatus()) {
                    results.add(result.conversionRequired() ? result.convert(data) : data);
                }
            } catch (JedisDataException var7) {
                DataAccessException dataAccessException = this.convertJedisAccessException(var7);
                if (cause == null) {
                    cause = dataAccessException;
                }

                results.add(dataAccessException);
            } catch (DataAccessException var8) {
                if (cause == null) {
                    cause = var8;
                }

                results.add(var8);
            }
        }

        if (cause != null) {
            throw new RedisPipelineException(cause, results);
        } else {
            return results;
        }
    }

    void pipeline(JedisResult result) {
        if (this.isQueueing()) {
            this.transaction(result);
        } else {
            this.pipelinedResults.add(result);
        }

    }

    void transaction(FutureResult<Response<?>> result) {
        this.txResults.add(result);
    }

    public byte[] echo(byte[] message) {
        try {
            if (this.isPipelined()) {
                this.pipeline(this.newJedisResult(this.getRequiredPipeline().echo(message)));
                return null;
            } else if (this.isQueueing()) {
                this.transaction(this.newJedisResult(this.getRequiredTransaction().echo(message)));
                return null;
            } else {
                return this.jedis.echo(message);
            }
        } catch (Exception var3) {
            throw this.convertJedisAccessException(var3);
        }
    }

    public String ping() {
        try {
            if (this.isPipelined()) {
                this.pipeline(this.newJedisResult(this.getRequiredPipeline().ping()));
                return null;
            } else if (this.isQueueing()) {
                this.transaction(this.newJedisResult(this.getRequiredTransaction().ping()));
                return null;
            } else {
                return this.jedis.ping();
            }
        } catch (Exception var2) {
            throw this.convertJedisAccessException(var2);
        }
    }

    public void discard() {
        try {
            if (!this.isPipelined()) {
                this.getRequiredTransaction().discard();
                return;
            }

            this.pipeline(this.newStatusResult(this.getRequiredPipeline().discard()));
        } catch (Exception var5) {
            throw this.convertJedisAccessException(var5);
        } finally {
            this.txResults.clear();
            this.transaction = null;
        }

    }

    public List<Object> exec() {
        List results;
        try {
            if (!this.isPipelined()) {
                if (this.transaction == null) {
                    throw new InvalidDataAccessApiUsageException("No ongoing transaction. Did you forget to call multi?");
                }

                results = this.transaction.exec();
                List var2 = !CollectionUtils.isEmpty(results) ? (new TransactionResultConverter(this.txResults, JedisConverters.exceptionConverter())).convert(results) : results;
                return var2;
            }

            this.pipeline(this.newJedisResult(this.getRequiredPipeline().exec(), new TransactionResultConverter(new LinkedList(this.txResults), JedisConverters.exceptionConverter())));
            results = null;
        } catch (Exception var6) {
            throw this.convertJedisAccessException(var6);
        } finally {
            this.txResults.clear();
            this.transaction = null;
        }

        return results;
    }

    @Nullable
    public Pipeline getPipeline() {
        return this.pipeline;
    }

    public Pipeline getRequiredPipeline() {
        Pipeline pipeline = this.getPipeline();
        if (pipeline == null) {
            throw new IllegalStateException("Connection has no active pipeline");
        } else {
            return pipeline;
        }
    }

    @Nullable
    public Transaction getTransaction() {
        return this.transaction;
    }

    public Transaction getRequiredTransaction() {
        Transaction transaction = this.getTransaction();
        if (transaction == null) {
            throw new IllegalStateException("Connection has no active transaction");
        } else {
            return transaction;
        }
    }

    public Jedis getJedis() {
        return this.jedis;
    }

    JedisResult newJedisResult(Response<?> response) {
        return JedisResultBuilder.forResponse(response).build();
    }

    <T, R> JedisResult newJedisResult(Response<T> response, Converter<T, R> converter) {
        return JedisResultBuilder.forResponse(response).mappedWith(converter).convertPipelineAndTxResults(this.convertPipelineAndTxResults).build();
    }

    <T, R> JedisResult newJedisResult(Response<T> response, Converter<T, R> converter, Supplier<R> defaultValue) {
        return JedisResultBuilder.forResponse(response).mappedWith(converter).convertPipelineAndTxResults(this.convertPipelineAndTxResults).mapNullTo(defaultValue).build();
    }

    JedisStatusResult newStatusResult(Response<?> response) {
        return JedisResultBuilder.forResponse(response).buildStatusResult();
    }

    public void multi() {
        if (!this.isQueueing()) {
            try {
                if (this.isPipelined()) {
                    this.getRequiredPipeline().multi();
                } else {
                    this.transaction = this.jedis.multi();
                }
            } catch (Exception var2) {
                throw this.convertJedisAccessException(var2);
            }
        }
    }

    public void select(int dbIndex) {
        try {
            if (this.isPipelined()) {
                this.pipeline(this.newStatusResult(this.getRequiredPipeline().select(dbIndex)));
            } else if (this.isQueueing()) {
                this.transaction(this.newStatusResult(this.getRequiredTransaction().select(dbIndex)));
            } else {
                this.jedis.select(dbIndex);
            }
        } catch (Exception var3) {
            throw this.convertJedisAccessException(var3);
        }
    }

    public void unwatch() {
        try {
            this.jedis.unwatch();
        } catch (Exception var2) {
            throw this.convertJedisAccessException(var2);
        }
    }

    public void watch(byte[]... keys) {
        if (this.isQueueing()) {
            throw new UnsupportedOperationException();
        } else {
            try {
                byte[][] var2 = keys;
                int var3 = keys.length;

                for(int var4 = 0; var4 < var3; ++var4) {
                    byte[] key = var2[var4];
                    if (this.isPipelined()) {
                        this.pipeline(this.newStatusResult(this.getRequiredPipeline().watch(new byte[][]{key})));
                    } else {
                        this.jedis.watch(new byte[][]{key});
                    }
                }

            } catch (Exception var6) {
                throw this.convertJedisAccessException(var6);
            }
        }
    }

    public Long publish(byte[] channel, byte[] message) {
        try {
            if (this.isPipelined()) {
                this.pipeline(this.newJedisResult(this.getRequiredPipeline().publish(channel, message)));
                return null;
            } else if (this.isQueueing()) {
                this.transaction(this.newJedisResult(this.getRequiredTransaction().publish(channel, message)));
                return null;
            } else {
                return this.jedis.publish(channel, message);
            }
        } catch (Exception var4) {
            throw this.convertJedisAccessException(var4);
        }
    }

    public Subscription getSubscription() {
        return this.subscription;
    }

    public boolean isSubscribed() {
        return this.subscription != null && this.subscription.isAlive();
    }

    public void pSubscribe(MessageListener listener, byte[]... patterns) {
        if (this.isSubscribed()) {
            throw new RedisSubscribedConnectionException("Connection already subscribed; use the connection Subscription to cancel or add new channels");
        } else if (this.isQueueing()) {
            throw new UnsupportedOperationException();
        } else if (this.isPipelined()) {
            throw new UnsupportedOperationException();
        } else {
            try {
                BinaryJedisPubSub jedisPubSub = new JedisMessageListener(listener);
                this.subscription = new JedisSubscription(listener, jedisPubSub, (byte[][])null, patterns);
                this.jedis.psubscribe(jedisPubSub, patterns);
            } catch (Exception var4) {
                throw this.convertJedisAccessException(var4);
            }
        }
    }

    public void subscribe(MessageListener listener, byte[]... channels) {
        if (this.isSubscribed()) {
            throw new RedisSubscribedConnectionException("Connection already subscribed; use the connection Subscription to cancel or add new channels");
        } else if (this.isQueueing()) {
            throw new UnsupportedOperationException();
        } else if (this.isPipelined()) {
            throw new UnsupportedOperationException();
        } else {
            try {
                BinaryJedisPubSub jedisPubSub = new JedisMessageListener(listener);
                this.subscription = new JedisSubscription(listener, jedisPubSub, channels, (byte[][])null);
                this.jedis.subscribe(jedisPubSub, channels);
            } catch (Exception var4) {
                throw this.convertJedisAccessException(var4);
            }
        }
    }

    public void setConvertPipelineAndTxResults(boolean convertPipelineAndTxResults) {
        this.convertPipelineAndTxResults = convertPipelineAndTxResults;
    }

    protected boolean isActive(RedisNode node) {
        Jedis temp = null;

        boolean var4;
        try {
            temp = this.getJedis(node);
            temp.connect();
            boolean var3 = temp.ping().equalsIgnoreCase("pong");
            return var3;
        } catch (Exception var8) {
            var4 = false;
        } finally {
            if (temp != null) {
                temp.disconnect();
                temp.close();
            }

        }

        return var4;
    }

    protected JedisSentinelConnection getSentinelConnection(RedisNode sentinel) {
        return new JedisSentinelConnection(this.getJedis(sentinel));
    }

    protected Jedis getJedis(RedisNode node) {
        Jedis jedis = new Jedis(node.getHost(), node.getPort());
        if (StringUtils.hasText(this.clientName)) {
            jedis.clientSetname(this.clientName);
        }

        return jedis;
    }
}
