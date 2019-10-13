package cn.choleece.base.framework.redis.connection;

import org.springframework.core.env.PropertySource;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author choleece
 * @Description: 哨兵模式配置
 * @Date 2019-10-13 22:47
 **/
public class RedisSentinelConfiguration implements RedisConfiguration, RedisConfiguration.SentinelConfiguration {

    /**
     * 哨兵模式主节点
     */
    private static final String REDIS_SENTINEL_MASTER_CONFIG_PROPERTY = "choleece.spring.redis.sentinel.master";

    /**
     * 哨兵模式节点集合
     */
    private static final String REDIS_SENTINEL_NODES_CONFIG_PROPERTY = "choleece.spring.redis.sentinel.nodes";

    @Nullable
    private NamedNode master;

    private Set<RedisNode> sentinels;

    private int database;

    private RedisPassword password;

    public RedisSentinelConfiguration() {
    }

    public RedisSentinelConfiguration(PropertySource<?> propertySource) {
        this.password = RedisPassword.none();

        Assert.notNull(propertySource, "PropertySource must not be null!");

        this.sentinels = new LinkedHashSet<>();

        if (propertySource.containsProperty(REDIS_SENTINEL_MASTER_CONFIG_PROPERTY)) {
            this.setMaster(propertySource.getProperty(REDIS_SENTINEL_MASTER_CONFIG_PROPERTY).toString());
        }

        if (propertySource.containsProperty(REDIS_SENTINEL_NODES_CONFIG_PROPERTY)) {

            /**
             * 将配置用,分隔开
             */
            this.appendSentinels(StringUtils.commaDelimitedListToSet(propertySource.getProperty("spring.redis.sentinel.nodes").toString()));
        }
    }

    public void setSentinels(Iterable<RedisNode> sentinels) {
        Assert.notNull(sentinels, "Cannot set sentinels to 'null'.");
        this.sentinels.clear();
        Iterator var2 = sentinels.iterator();

        while(var2.hasNext()) {
            RedisNode sentinel = (RedisNode)var2.next();
            this.addSentinel(sentinel);
        }
    }

    @Override
    public Set<RedisNode> getSentinels() {
        return Collections.unmodifiableSet(this.sentinels);
    }

    public void addSentinel(RedisNode sentinel) {
        Assert.notNull(sentinel, "Sentinel must not be 'null'.");
        this.sentinels.add(sentinel);
    }

    @Override
    public void setMaster(NamedNode master) {
        Assert.notNull(master, "Sentinel master node must not be 'null'.");
        this.master = master;
    }

    @Override
    public NamedNode getMaster() {
        return this.master;
    }

    public RedisSentinelConfiguration master(String master) {
        this.setMaster(master);
        return this;
    }

    public RedisSentinelConfiguration master(NamedNode master) {
        this.setMaster(master);
        return this;
    }

    public RedisSentinelConfiguration sentinel(RedisNode sentinel) {
        this.addSentinel(sentinel);
        return this;
    }

    public RedisSentinelConfiguration sentinel(String host, Integer port) {
        return this.sentinel(new RedisNode(host, port));
    }

    private void appendSentinels(Set<String> hostAndPorts) {
        Iterator var2 = hostAndPorts.iterator();

        while(var2.hasNext()) {
            String hostAndPort = (String)var2.next();
            this.addSentinel(this.readHostAndPortFromString(hostAndPort));
        }

    }

    @Override
    public int getDatabase() {
        return this.database;
    }

    @Override
    public void setDatabase(int index) {
        Assert.isTrue(index >= 0, () -> {
            return String.format("Invalid DB index '%s' (a positive index required)", index);
        });
        this.database = index;
    }

    @Override
    public RedisPassword getPassword() {
        return this.password;
    }

    @Override
    public void setPassword(RedisPassword password) {
        Assert.notNull(password, "RedisPassword must not be null!");
        this.password = password;
    }

    private RedisNode readHostAndPortFromString(String hostAndPort) {
        String[] args = StringUtils.split(hostAndPort, ":");
        Assert.notNull(args, "HostAndPort need to be seperated by  ':'.");
        Assert.isTrue(args.length == 2, "Host and Port String needs to specified as host:port");
        return new RedisNode(args[0], Integer.valueOf(args[1]));
    }

    private static Map<String, Object> asMap(String master, Set<String> sentinelHostAndPorts) {
        Assert.hasText(master, "Master address must not be null or empty!");
        Assert.notNull(sentinelHostAndPorts, "SentinelHostAndPorts must not be null!");
        Map<String, Object> map = new HashMap(2, 1);
        map.put(REDIS_SENTINEL_MASTER_CONFIG_PROPERTY, master);
        map.put(REDIS_SENTINEL_NODES_CONFIG_PROPERTY, StringUtils.collectionToCommaDelimitedString(sentinelHostAndPorts));
        return map;
    }
}
