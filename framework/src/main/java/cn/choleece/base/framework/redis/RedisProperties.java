package cn.choleece.base.framework.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

/**
 * @author choleece
 * @Description: Redis 配置属性
 * @Date 2019-10-13 21:09
 **/
@ConfigurationProperties(prefix = "choleece.spring.redis")
public class RedisProperties {

    /**
     * 连接使用的数据库 默认第一个(从0开始)
     */
    private int database = 0;

    /**
     * 连接地址 会覆盖 host, port, and password. User is ignored
     *
     * 格式如下
     * redis://user:password@example.com:6379
     */
    private String url;

    /**
     * redis 地址，默认本地
     */
    private String host = "localhost";

    /**
     * 登陆redis服务的密码
     */
    private String password;

    /**
     * redis 端口 默认6379
     */
    private int port = 6379;

    /**
     * 是否开启SSL支持
     */
    private boolean ssl;

    /**
     * 链接超时时间
     */
    private Duration timeout;

    /**
     * 哨兵模式
     */
    private Sentinel sentinel;

    /**
     * 集群模式
     */
    private Cluster cluster;

    /**
     * 使用Jedis客户端，这里用final修饰，表示不可更改
     *
     * spring boot redis里还有另外一个客户端 Lettuce，这里暂时不做讨论
     */
    private final Jedis jedis = new Jedis();

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isSsl() {
        return ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    public Duration getTimeout() {
        return timeout;
    }

    public void setTimeout(Duration timeout) {
        this.timeout = timeout;
    }

    public Sentinel getSentinel() {
        return sentinel;
    }

    public void setSentinel(Sentinel sentinel) {
        this.sentinel = sentinel;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public Jedis getJedis() {
        return jedis;
    }

    /**
     * 连接池属性
     * 联系一下线程池，想想异同点
     */
    public static class Pool {

        /**
         * Maximum number of "idle" connections in the pool. Use a negative value to
         * indicate an unlimited number of idle connections.
         *
         * 链接池中最大的空闲链接数 用负数表示池中可以存在不受限的空闲连接（比较浪费资源）
         */
        private int maxIdle = 8;

        /**
         * Target for the minimum number of idle connections to maintain in the pool. This
         * setting only has an effect if both it and time between eviction runs are
         * positive.
         *
         * 连接池中最小的空闲连接数
         */
        private int minIdle = 0;

        /**
         * Maximum number of connections that can be allocated by the pool at a given
         * time. Use a negative value for no limit.
         *
         * 连接池中在特定时间内能分配的最大连接数，用负数表示不受限制
         */
        private int maxActive = 8;

        /**
         * Maximum amount of time a connection allocation should block before throwing an
         * exception when the pool is exhausted. Use a negative value to block
         * indefinitely.
         *
         * 连接等待分配的最大时间，如果超过时间，则会抛出异常，如果是负数，则无限期等待
         */
        private Duration maxWait = Duration.ofMillis(-1);

        /**
         * Time between runs of the idle object evictor thread. When positive, the idle
         * object evictor thread starts, otherwise no idle object eviction is performed.
         *
         * 空闲连接存活的时间（如果空闲连接长时间没有被使用，则会有evictor 线程将其清理掉，
         * 但又为了保持池里的线程保持在minIdle的数量，池会建立新的空闲连接，如此反复）
         * 如果设置的是负数，则不执行空闲连接清理动作
         *
         * 比如有些数据库连接的时候有超时限制（mysql连接在8小时后断开），
         * 或者由于网络中断等原因，连接池的连接会出现失效的情况，这时候设置一个testWhileIdle参数为true，
         * 可以保证连接池内部定时检测连接的可用性，不可用的连接会被抛弃或者重建，最大情况的保证从连接池中得到的Connection对象是可用的
         *
         * 参考：http://www.mamicode.com/info-detail-1242126.html
         */
        private Duration timeBetweenEvictionRuns;

        public int getMaxIdle() {
            return maxIdle;
        }

        public void setMaxIdle(int maxIdle) {
            this.maxIdle = maxIdle;
        }

        public int getMinIdle() {
            return minIdle;
        }

        public void setMinIdle(int minIdle) {
            this.minIdle = minIdle;
        }

        public int getMaxActive() {
            return maxActive;
        }

        public void setMaxActive(int maxActive) {
            this.maxActive = maxActive;
        }

        public Duration getMaxWait() {
            return maxWait;
        }

        public void setMaxWait(Duration maxWait) {
            this.maxWait = maxWait;
        }

        public Duration getTimeBetweenEvictionRuns() {
            return timeBetweenEvictionRuns;
        }

        public void setTimeBetweenEvictionRuns(Duration timeBetweenEvictionRuns) {
            this.timeBetweenEvictionRuns = timeBetweenEvictionRuns;
        }
    }

    public static class Sentinel {

        /**
         * Name of the Redis server.
         */
        private String master;

        /**
         * Comma-separated list of "host:port" pair
         * 用逗号分开的host:port列表，如 127.0.0.1:6379,192.168.1.17:6379
         */
        private List<String> nodes;

        public String getMaster() {
            return master;
        }

        public void setMaster(String master) {
            this.master = master;
        }

        public List<String> getNodes() {
            return nodes;
        }

        public void setNodes(List<String> nodes) {
            this.nodes = nodes;
        }
    }

    public static class Cluster {

        /**
         * Comma-separated list of "host:port" pairs to bootstrap from. This represents an
         * "initial" list of cluster nodes and is required to have at least one entry.
         *
         * 用逗号分开的host:port列表，如 127.0.0.1:6379,192.168.1.17:6379
         * 这些列表表示集群的初始列表，必须至少包含一个（这里说的初始的意思，表面可以动态增减节点）
         */
        private List<String> nodes;

        /**
         * Maximum number of redirects to follow when executing commands across the
         * cluster.
         *
         * 在群集上执行命令时重定向的最大数量（目前还不是特别清楚这个字段的含义）
         */
        private Integer maxRedirects;

        public List<String> getNodes() {
            return nodes;
        }

        public void setNodes(List<String> nodes) {
            this.nodes = nodes;
        }

        public Integer getMaxRedirects() {
            return maxRedirects;
        }

        public void setMaxRedirects(Integer maxRedirects) {
            this.maxRedirects = maxRedirects;
        }
    }

    /**
     * Jedis配置
     */
    public static class Jedis {

        /**
         * Jedis 连接池配置
         */
        private Pool pool;

        public Pool getPool() {
            return pool;
        }

        public void setPool(Pool pool) {
            this.pool = pool;
        }
    }
}
