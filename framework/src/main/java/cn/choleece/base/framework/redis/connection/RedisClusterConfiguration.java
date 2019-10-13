package cn.choleece.base.framework.redis.connection;

import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author choleece
 * @Description: Redis 集群模式配置
 * @Date 2019-10-13 22:59
 **/
public class RedisClusterConfiguration implements RedisConfiguration, RedisConfiguration.ClusterConfiguration {

    private static final String REDIS_CLUSTER_NODES_CONFIG_PROPERTY = "choleece.spring.redis.cluster.nodes";

    private static final String REDIS_CLUSTER_MAX_REDIRECTS_CONFIG_PROPERTY = "choleece.spring.redis.cluster.max-redirects";

    /**
     * 集群节点
     */
    private Set<RedisNode> clusterNodes;

    @Nullable
    private Integer maxRedirects;

    private RedisPassword password;

    public RedisClusterConfiguration() {
        this((PropertySource)(new MapPropertySource("RedisClusterConfiguration", Collections.emptyMap())));
    }

    public RedisClusterConfiguration(Collection<String> clusterNodes) {
        this((PropertySource)(new MapPropertySource("RedisClusterConfiguration", asMap(clusterNodes, -1))));
    }

    public RedisClusterConfiguration(PropertySource<?> propertySource) {
        this.password = RedisPassword.none();
        Assert.notNull(propertySource, "PropertySource must not be null!");
        this.clusterNodes = new LinkedHashSet();
        if (propertySource.containsProperty(REDIS_CLUSTER_NODES_CONFIG_PROPERTY)) {
            this.appendClusterNodes(StringUtils.commaDelimitedListToSet(propertySource.getProperty("spring.redis.cluster.nodes").toString()));
        }

        if (propertySource.containsProperty(REDIS_CLUSTER_MAX_REDIRECTS_CONFIG_PROPERTY)) {
            this.maxRedirects = NumberUtils.parseNumber(propertySource.getProperty("spring.redis.cluster.max-redirects").toString(), Integer.class);
        }
    }

    public void setClusterNodes(Iterable<RedisNode> nodes) {
        Assert.notNull(nodes, "Cannot set cluster nodes to 'null'.");
        this.clusterNodes.clear();
        Iterator var2 = nodes.iterator();

        while(var2.hasNext()) {
            RedisNode clusterNode = (RedisNode)var2.next();
            this.addClusterNode(clusterNode);
        }
    }

    @Override
    public Set<RedisNode> getClusterNodes() {
        return Collections.unmodifiableSet(this.clusterNodes);
    }

    public void addClusterNode(RedisNode node) {
        Assert.notNull(node, "ClusterNode must not be 'null'.");
        this.clusterNodes.add(node);
    }

    public RedisClusterConfiguration clusterNode(RedisNode node) {
        this.clusterNodes.add(node);
        return this;
    }

    @Override
    public Integer getMaxRedirects() {
        return this.maxRedirects != null && this.maxRedirects > -2147483648 ? this.maxRedirects : null;
    }

    public void setMaxRedirects(int maxRedirects) {
        Assert.isTrue(maxRedirects >= 0, "MaxRedirects must be greater or equal to 0");
        this.maxRedirects = maxRedirects;
    }

    public RedisClusterConfiguration clusterNode(String host, Integer port) {
        return this.clusterNode(new RedisNode(host, port));
    }

    private void appendClusterNodes(Set<String> hostAndPorts) {
        Iterator var2 = hostAndPorts.iterator();

        while(var2.hasNext()) {
            String hostAndPort = (String)var2.next();
            this.addClusterNode(this.readHostAndPortFromString(hostAndPort));
        }
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

    private static Map<String, Object> asMap(Collection<String> clusterHostAndPorts, int redirects) {
        Assert.notNull(clusterHostAndPorts, "ClusterHostAndPorts must not be null!");
        Map<String, Object> map = new HashMap(2, 1);
        map.put(REDIS_CLUSTER_NODES_CONFIG_PROPERTY, StringUtils.collectionToCommaDelimitedString(clusterHostAndPorts));
        if (redirects >= 0) {
            map.put(REDIS_CLUSTER_MAX_REDIRECTS_CONFIG_PROPERTY, redirects);
        }

        return map;
    }
}
