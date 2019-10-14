package cn.choleece.base.framework.redis.connection.jedis;

import cn.choleece.base.framework.redis.ExceptionTranslationStrategy;
import cn.choleece.base.framework.redis.PassThroughExceptionTranslationStrategy;
import cn.choleece.base.framework.redis.connection.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.util.Pool;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocketFactory;
import java.time.Duration;
import java.util.*;

/**
 * @description: Jedis 连接工厂
 * @author: sf
 * @time: 2019-10-14 15:25
 */
public class JedisConnectionFactory implements InitializingBean, DisposableBean, RedisConnectionFactory {

    /**
     * 日志
     */
    private static final Log log = LogFactory.getLog(JedisConnectionFactory.class);

    /**
     * 异常策略
     */
    private static final ExceptionTranslationStrategy EXCEPTION_TRANSLATION = new PassThroughExceptionTranslationStrategy(JedisConverters.exceptionConverter());

    /**
     * Jedis 客户端配置
     */
    private final JedisClientConfiguration clientConfiguration;

    /**
     * ShardedJedis是通过一致性哈希来实现分布式缓存的，通过一定的策略把不同的key分配到不同的redis server上，达到横向扩展的目的
     *
     * 原生的集群方式是通过Hash Slot (Hash槽)实现的
     *
     * 参考:https://www.cnblogs.com/vhua/p/redis_1.html
     */
    @Nullable
    private JedisShardInfo shardInfo;

    private boolean providedShardInfo;

    @Nullable
    private Pool<Jedis> pool;

    private boolean convertPipelineAndTxResults;

    /**
     * Redis 单节点配置
     */
    private RedisStandaloneConfiguration standaloneConfig;

    /**
     * Redis 配置
     */
    @Nullable
    private RedisConfiguration configuration;

    /**
     * Jedis 集群
     */
    @Nullable
    private JedisCluster cluster;

    @Nullable
    private ClusterCommandExecutor clusterCommandExecutor;

    public JedisConnectionFactory() {
        this((JedisClientConfiguration)(new JedisConnectionFactory.MutableJedisClientConfiguration()));
    }

    private JedisConnectionFactory(JedisClientConfiguration clientConfig) {
        this.providedShardInfo = false;
        this.convertPipelineAndTxResults = true;
        this.standaloneConfig = new RedisStandaloneConfiguration("localhost", 6379);
        Assert.notNull(clientConfig, "JedisClientConfiguration must not be null!");
        this.clientConfiguration = clientConfig;
    }

    /** @deprecated */
    @Deprecated
    public JedisConnectionFactory(JedisShardInfo shardInfo) {
        this(JedisConnectionFactory.MutableJedisClientConfiguration.create(shardInfo));
        this.shardInfo = shardInfo;
        this.providedShardInfo = true;
    }

    public JedisConnectionFactory(JedisPoolConfig poolConfig) {
        this((RedisSentinelConfiguration)null, poolConfig);
    }

    public JedisConnectionFactory(RedisSentinelConfiguration sentinelConfig) {
        this((RedisSentinelConfiguration)sentinelConfig, (JedisClientConfiguration)(new JedisConnectionFactory.MutableJedisClientConfiguration()));
    }

    public JedisConnectionFactory(RedisSentinelConfiguration sentinelConfig, JedisPoolConfig poolConfig) {
        this.providedShardInfo = false;
        this.convertPipelineAndTxResults = true;
        this.standaloneConfig = new RedisStandaloneConfiguration("localhost", 6379);
        this.configuration = sentinelConfig;
        this.clientConfiguration = JedisConnectionFactory.MutableJedisClientConfiguration.create((GenericObjectPoolConfig)(poolConfig != null ? poolConfig : new JedisPoolConfig()));
    }

    public JedisConnectionFactory(RedisClusterConfiguration clusterConfig) {
        this((RedisClusterConfiguration)clusterConfig, (JedisClientConfiguration)(new JedisConnectionFactory.MutableJedisClientConfiguration()));
    }

    public JedisConnectionFactory(RedisClusterConfiguration clusterConfig, JedisPoolConfig poolConfig) {
        this.providedShardInfo = false;
        this.convertPipelineAndTxResults = true;
        this.standaloneConfig = new RedisStandaloneConfiguration("localhost", 6379);
        Assert.notNull(clusterConfig, "RedisClusterConfiguration must not be null!");
        this.configuration = clusterConfig;
        this.clientConfiguration = JedisConnectionFactory.MutableJedisClientConfiguration.create((GenericObjectPoolConfig)poolConfig);
    }

    public JedisConnectionFactory(RedisStandaloneConfiguration standaloneConfig) {
        this((RedisStandaloneConfiguration)standaloneConfig, (JedisClientConfiguration)(new JedisConnectionFactory.MutableJedisClientConfiguration()));
    }

    public JedisConnectionFactory(RedisStandaloneConfiguration standaloneConfig, JedisClientConfiguration clientConfig) {
        this(clientConfig);
        Assert.notNull(standaloneConfig, "RedisStandaloneConfiguration must not be null!");
        this.standaloneConfig = standaloneConfig;
    }

    public JedisConnectionFactory(RedisSentinelConfiguration sentinelConfig, JedisClientConfiguration clientConfig) {
        this(clientConfig);
        Assert.notNull(sentinelConfig, "RedisSentinelConfiguration must not be null!");
        this.configuration = sentinelConfig;
    }

    public JedisConnectionFactory(RedisClusterConfiguration clusterConfig, JedisClientConfiguration clientConfig) {
        this(clientConfig);
        Assert.notNull(clusterConfig, "RedisClusterConfiguration must not be null!");
        this.configuration = clusterConfig;
    }

    protected Jedis fetchJedisConnector() {
        try {
            if (this.getUsePool() && this.pool != null) {
                return (Jedis)this.pool.getResource();
            } else {
                Jedis jedis = this.createJedis();
                jedis.connect();
                this.potentiallySetClientName(jedis);
                return jedis;
            }
        } catch (Exception var2) {
            throw new RedisConnectionFailureException("Cannot get Jedis connection", var2);
        }
    }

    private Jedis createJedis() {
        if (this.providedShardInfo) {
            return new Jedis(this.getShardInfo());
        } else {
            Jedis jedis = new Jedis(this.getHostName(), this.getPort(), this.getConnectTimeout(), this.getReadTimeout(), this.isUseSsl(), (SSLSocketFactory)this.clientConfiguration.getSslSocketFactory().orElse((Object)null), (SSLParameters)this.clientConfiguration.getSslParameters().orElse((Object)null), (HostnameVerifier)this.clientConfiguration.getHostnameVerifier().orElse((Object)null));
            Client client = jedis.getClient();
            this.getRedisPassword().map(String::new).ifPresent(client::setPassword);
            client.setDb((long)this.getDatabase());
            return jedis;
        }
    }

    protected JedisConnection postProcessConnection(JedisConnection connection) {
        return connection;
    }

    public void afterPropertiesSet() {
        if (this.shardInfo == null && this.clientConfiguration instanceof JedisConnectionFactory.MutableJedisClientConfiguration) {
            this.providedShardInfo = false;
            this.shardInfo = new JedisShardInfo(this.getHostName(), this.getPort(), this.isUseSsl(), (SSLSocketFactory)this.clientConfiguration.getSslSocketFactory().orElse((Object)null), (SSLParameters)this.clientConfiguration.getSslParameters().orElse((Object)null), (HostnameVerifier)this.clientConfiguration.getHostnameVerifier().orElse((Object)null));
            Optional var10000 = this.getRedisPassword().map(String::new);
            JedisShardInfo var10001 = this.shardInfo;
            var10000.ifPresent(var10001::setPassword);
            int readTimeout = this.getReadTimeout();
            if (readTimeout > 0) {
                this.shardInfo.setSoTimeout(readTimeout);
            }

            this.getMutableConfiguration().setShardInfo(this.shardInfo);
        }

        if (this.getUsePool() && !this.isRedisClusterAware()) {
            this.pool = this.createPool();
        }

        if (this.isRedisClusterAware()) {
            this.cluster = this.createCluster();
        }

    }

    private Pool<Jedis> createPool() {
        return this.isRedisSentinelAware() ? this.createRedisSentinelPool((RedisSentinelConfiguration)this.configuration) : this.createRedisPool();
    }

    protected Pool<Jedis> createRedisSentinelPool(RedisSentinelConfiguration config) {
        GenericObjectPoolConfig poolConfig = this.getPoolConfig() != null ? this.getPoolConfig() : new JedisPoolConfig();
        return new JedisSentinelPool(config.getMaster().getName(), this.convertToJedisSentinelSet(config.getSentinels()), (GenericObjectPoolConfig)poolConfig, this.getConnectTimeout(), this.getReadTimeout(), this.getPassword(), this.getDatabase(), this.getClientName());
    }

    protected Pool<Jedis> createRedisPool() {
        return new JedisPool(this.getPoolConfig(), this.getHostName(), this.getPort(), this.getConnectTimeout(), this.getReadTimeout(), this.getPassword(), this.getDatabase(), this.getClientName(), this.isUseSsl(), (SSLSocketFactory)this.clientConfiguration.getSslSocketFactory().orElse((Object)null), (SSLParameters)this.clientConfiguration.getSslParameters().orElse((Object)null), (HostnameVerifier)this.clientConfiguration.getHostnameVerifier().orElse((Object)null));
    }

    private JedisCluster createCluster() {
        JedisCluster cluster = this.createCluster((RedisClusterConfiguration)this.configuration, this.getPoolConfig());
        JedisClusterTopologyProvider topologyProvider = new JedisClusterTopologyProvider(cluster);
        this.clusterCommandExecutor = new ClusterCommandExecutor(topologyProvider, new JedisClusterNodeResourceProvider(cluster, topologyProvider), EXCEPTION_TRANSLATION);
        return cluster;
    }

    protected JedisCluster createCluster(RedisClusterConfiguration clusterConfig, GenericObjectPoolConfig poolConfig) {
        Assert.notNull(clusterConfig, "Cluster configuration must not be null!");
        Set<HostAndPort> hostAndPort = new HashSet();
        Iterator var4 = clusterConfig.getClusterNodes().iterator();

        while(var4.hasNext()) {
            RedisNode node = (RedisNode)var4.next();
            hostAndPort.add(new HostAndPort(node.getHost(), node.getPort()));
        }

        int redirects = clusterConfig.getMaxRedirects() != null ? clusterConfig.getMaxRedirects() : 5;
        int connectTimeout = this.getConnectTimeout();
        int readTimeout = this.getReadTimeout();
        return StringUtils.hasText(this.getPassword()) ? new JedisCluster(hostAndPort, connectTimeout, readTimeout, redirects, this.getPassword(), poolConfig) : new JedisCluster(hostAndPort, connectTimeout, readTimeout, redirects, poolConfig);
    }

    public void destroy() {
        if (this.getUsePool() && this.pool != null) {
            try {
                this.pool.destroy();
            } catch (Exception var4) {
                log.warn("Cannot properly close Jedis pool", var4);
            }

            this.pool = null;
        }

        if (this.cluster != null) {
            try {
                this.cluster.close();
            } catch (Exception var3) {
                log.warn("Cannot properly close Jedis cluster", var3);
            }

            try {
                this.clusterCommandExecutor.destroy();
            } catch (Exception var2) {
                log.warn("Cannot properly close cluster command executor", var2);
            }
        }
    }

    public RedisConnection getConnection() {
        if (this.isRedisClusterAware()) {
            return this.getClusterConnection();
        } else {
            Jedis jedis = this.fetchJedisConnector();
            String clientName = (String)this.clientConfiguration.getClientName().orElse((Object)null);
            JedisConnection connection = this.getUsePool() ? new JedisConnection(jedis, this.pool, this.getDatabase(), clientName) : new JedisConnection(jedis, (Pool)null, this.getDatabase(), clientName);
            connection.setConvertPipelineAndTxResults(this.convertPipelineAndTxResults);
            return this.postProcessConnection(connection);
        }
    }

    public RedisClusterConnection getClusterConnection() {
        if (!this.isRedisClusterAware()) {
            throw new InvalidDataAccessApiUsageException("Cluster is not configured!");
        } else {
            return new JedisClusterConnection(this.cluster, this.clusterCommandExecutor);
        }
    }

    public DataAccessException translateExceptionIfPossible(RuntimeException ex) {
        return EXCEPTION_TRANSLATION.translate(ex);
    }

    public String getHostName() {
        return this.standaloneConfig.getHostName();
    }

    /** @deprecated */
    @Deprecated
    public void setHostName(String hostName) {
        this.standaloneConfig.setHostName(hostName);
    }

    public boolean isUseSsl() {
        return this.clientConfiguration.isUseSsl();
    }

    /** @deprecated */
    @Deprecated
    public void setUseSsl(boolean useSsl) {
        this.getMutableConfiguration().setUseSsl(useSsl);
    }

    public String getPassword() {
        return (String)this.getRedisPassword().map(String::new).orElse((Object)null);
    }

    private RedisPassword getRedisPassword() {
        RedisConfiguration var10000 = this.configuration;
        RedisStandaloneConfiguration var10001 = this.standaloneConfig;
        var10001.getClass();
        return RedisConfiguration.getPasswordOrElse(var10000, var10001::getPassword);
    }

    /** @deprecated */
    @Deprecated
    public void setPassword(String password) {
        if (RedisConfiguration.isPasswordAware(this.configuration)) {
            ((WithPassword)this.configuration).setPassword(password);
        } else {
            this.standaloneConfig.setPassword(RedisPassword.of(password));
        }
    }

    public int getPort() {
        return this.standaloneConfig.getPort();
    }

    /** @deprecated */
    @Deprecated
    public void setPort(int port) {
        this.standaloneConfig.setPort(port);
    }

    /** @deprecated */
    @Deprecated
    @Nullable
    public JedisShardInfo getShardInfo() {
        return this.shardInfo;
    }

    /** @deprecated */
    @Deprecated
    public void setShardInfo(JedisShardInfo shardInfo) {
        this.shardInfo = shardInfo;
        this.providedShardInfo = true;
        this.getMutableConfiguration().setShardInfo(shardInfo);
    }

    public int getTimeout() {
        return this.getReadTimeout();
    }

    /** @deprecated */
    @Deprecated
    public void setTimeout(int timeout) {
        this.getMutableConfiguration().setReadTimeout(Duration.ofMillis((long)timeout));
        this.getMutableConfiguration().setConnectTimeout(Duration.ofMillis((long)timeout));
    }

    public boolean getUsePool() {
        return this.isRedisSentinelAware() ? true : this.clientConfiguration.isUsePooling();
    }

    /** @deprecated */
    @Deprecated
    public void setUsePool(boolean usePool) {
        if (this.isRedisSentinelAware() && !usePool) {
            throw new IllegalStateException("Jedis requires pooling for Redis Sentinel use!");
        } else {
            this.getMutableConfiguration().setUsePooling(usePool);
        }
    }

    @Nullable
    public GenericObjectPoolConfig getPoolConfig() {
        return (GenericObjectPoolConfig)this.clientConfiguration.getPoolConfig().orElse((Object)null);
    }

    /** @deprecated */
    @Deprecated
    public void setPoolConfig(JedisPoolConfig poolConfig) {
        this.getMutableConfiguration().setPoolConfig(poolConfig);
    }

    public int getDatabase() {
        RedisConfiguration var10000 = this.configuration;
        RedisStandaloneConfiguration var10001 = this.standaloneConfig;
        var10001.getClass();
        return RedisConfiguration.getDatabaseOrElse(var10000, var10001::getDatabase);
    }

    /** @deprecated */
    @Deprecated
    public void setDatabase(int index) {
        Assert.isTrue(index >= 0, "invalid DB index (a positive index required)");
        if (RedisConfiguration.isDatabaseIndexAware(this.configuration)) {
            ((WithDatabaseIndex)this.configuration).setDatabase(index);
        } else {
            this.standaloneConfig.setDatabase(index);
        }
    }

    @Nullable
    public String getClientName() {
        return (String)this.clientConfiguration.getClientName().orElse((Object)null);
    }

    /** @deprecated */
    @Deprecated
    public void setClientName(String clientName) {
        this.getMutableConfiguration().setClientName(clientName);
    }

    public JedisClientConfiguration getClientConfiguration() {
        return this.clientConfiguration;
    }

    @Nullable
    public RedisStandaloneConfiguration getStandaloneConfiguration() {
        return this.standaloneConfig;
    }

    @Nullable
    public RedisSentinelConfiguration getSentinelConfiguration() {
        return RedisConfiguration.isSentinelConfiguration(this.configuration) ? (RedisSentinelConfiguration)this.configuration : null;
    }

    @Nullable
    public RedisClusterConfiguration getClusterConfiguration() {
        return RedisConfiguration.isClusterConfiguration(this.configuration) ? (RedisClusterConfiguration)this.configuration : null;
    }

    public boolean getConvertPipelineAndTxResults() {
        return this.convertPipelineAndTxResults;
    }

    public void setConvertPipelineAndTxResults(boolean convertPipelineAndTxResults) {
        this.convertPipelineAndTxResults = convertPipelineAndTxResults;
    }

    public boolean isRedisSentinelAware() {
        return RedisConfiguration.isSentinelConfiguration(this.configuration);
    }

    public boolean isRedisClusterAware() {
        return RedisConfiguration.isClusterConfiguration(this.configuration);
    }

    public RedisSentinelConnection getSentinelConnection() {
        if (!this.isRedisSentinelAware()) {
            throw new InvalidDataAccessResourceUsageException("No Sentinels configured");
        } else {
            return new JedisSentinelConnection(this.getActiveSentinel());
        }
    }

    private Jedis getActiveSentinel() {
        Assert.isTrue(RedisConfiguration.isSentinelConfiguration(this.configuration), "SentinelConfig must not be null!");
        Iterator var1 = ((SentinelConfiguration)this.configuration).getSentinels().iterator();

        Jedis jedis;
        do {
            if (!var1.hasNext()) {
                throw new InvalidDataAccessResourceUsageException("No Sentinel found");
            }

            RedisNode node = (RedisNode)var1.next();
            jedis = new Jedis(node.getHost(), node.getPort(), this.getConnectTimeout(), this.getReadTimeout());
        } while(!jedis.ping().equalsIgnoreCase("pong"));

        this.potentiallySetClientName(jedis);
        return jedis;
    }

    private Set<String> convertToJedisSentinelSet(Collection<RedisNode> nodes) {
        if (CollectionUtils.isEmpty(nodes)) {
            return Collections.emptySet();
        } else {
            Set<String> convertedNodes = new LinkedHashSet(nodes.size());
            Iterator var3 = nodes.iterator();

            while(var3.hasNext()) {
                RedisNode node = (RedisNode)var3.next();
                if (node != null) {
                    convertedNodes.add(node.asString());
                }
            }

            return convertedNodes;
        }
    }

    private void potentiallySetClientName(Jedis jedis) {
        this.clientConfiguration.getClientName().ifPresent(jedis::clientSetname);
    }

    private int getReadTimeout() {
        return Math.toIntExact(this.clientConfiguration.getReadTimeout().toMillis());
    }

    private int getConnectTimeout() {
        return Math.toIntExact(this.clientConfiguration.getConnectTimeout().toMillis());
    }

    private JedisConnectionFactory.MutableJedisClientConfiguration getMutableConfiguration() {
        Assert.state(this.clientConfiguration instanceof JedisConnectionFactory.MutableJedisClientConfiguration, () -> {
            return String.format("Client configuration must be instance of MutableJedisClientConfiguration but is %s", ClassUtils.getShortName(this.clientConfiguration.getClass()));
        });
        return (JedisConnectionFactory.MutableJedisClientConfiguration)this.clientConfiguration;
    }

    static class MutableJedisClientConfiguration implements JedisClientConfiguration {
        private boolean useSsl;
        @Nullable
        private SSLSocketFactory sslSocketFactory;
        @Nullable
        private SSLParameters sslParameters;
        @Nullable
        private HostnameVerifier hostnameVerifier;
        private boolean usePooling = true;
        private GenericObjectPoolConfig poolConfig = new JedisPoolConfig();
        @Nullable
        private String clientName;
        private Duration readTimeout = Duration.ofMillis(2000L);
        private Duration connectTimeout = Duration.ofMillis(2000L);

        MutableJedisClientConfiguration() {
        }

        public static JedisClientConfiguration create(JedisShardInfo shardInfo) {
            JedisConnectionFactory.MutableJedisClientConfiguration configuration = new JedisConnectionFactory.MutableJedisClientConfiguration();
            configuration.setShardInfo(shardInfo);
            return configuration;
        }

        public static JedisClientConfiguration create(GenericObjectPoolConfig jedisPoolConfig) {
            JedisConnectionFactory.MutableJedisClientConfiguration configuration = new JedisConnectionFactory.MutableJedisClientConfiguration();
            configuration.setPoolConfig(jedisPoolConfig);
            return configuration;
        }

        public boolean isUseSsl() {
            return this.useSsl;
        }

        public void setUseSsl(boolean useSsl) {
            this.useSsl = useSsl;
        }

        public Optional<SSLSocketFactory> getSslSocketFactory() {
            return Optional.ofNullable(this.sslSocketFactory);
        }

        public void setSslSocketFactory(SSLSocketFactory sslSocketFactory) {
            this.sslSocketFactory = sslSocketFactory;
        }

        public Optional<SSLParameters> getSslParameters() {
            return Optional.ofNullable(this.sslParameters);
        }

        public void setSslParameters(SSLParameters sslParameters) {
            this.sslParameters = sslParameters;
        }

        public Optional<HostnameVerifier> getHostnameVerifier() {
            return Optional.ofNullable(this.hostnameVerifier);
        }

        public void setHostnameVerifier(HostnameVerifier hostnameVerifier) {
            this.hostnameVerifier = hostnameVerifier;
        }

        public boolean isUsePooling() {
            return this.usePooling;
        }

        public void setUsePooling(boolean usePooling) {
            this.usePooling = usePooling;
        }

        public Optional<GenericObjectPoolConfig> getPoolConfig() {
            return Optional.ofNullable(this.poolConfig);
        }

        public void setPoolConfig(GenericObjectPoolConfig poolConfig) {
            this.poolConfig = poolConfig;
        }

        public Optional<String> getClientName() {
            return Optional.ofNullable(this.clientName);
        }

        public void setClientName(String clientName) {
            this.clientName = clientName;
        }

        public Duration getReadTimeout() {
            return this.readTimeout;
        }

        public void setReadTimeout(Duration readTimeout) {
            this.readTimeout = readTimeout;
        }

        public Duration getConnectTimeout() {
            return this.connectTimeout;
        }

        public void setConnectTimeout(Duration connectTimeout) {
            this.connectTimeout = connectTimeout;
        }

        public void setShardInfo(JedisShardInfo shardInfo) {
            this.setSslSocketFactory(shardInfo.getSslSocketFactory());
            this.setSslParameters(shardInfo.getSslParameters());
            this.setHostnameVerifier(shardInfo.getHostnameVerifier());
            this.setUseSsl(shardInfo.getSsl());
            this.setConnectTimeout(Duration.ofMillis((long)shardInfo.getConnectionTimeout()));
            this.setReadTimeout(Duration.ofMillis((long)shardInfo.getSoTimeout()));
        }
    }
}
