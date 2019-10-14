package cn.choleece.base.framework.redis.connection.jedis;

import cn.choleece.base.framework.redis.connection.NamedNode;
import cn.choleece.base.framework.redis.connection.RedisNode;
import cn.choleece.base.framework.redis.connection.RedisSentinelConnection;
import cn.choleece.base.framework.redis.connection.RedisServer;
import org.springframework.util.Assert;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.List;

/**
 * @description: Jedis 哨兵模式连接
 * @author: sf
 * @time: 2019-10-14 17:00
 */
public class JedisSentinelConnection implements RedisSentinelConnection {

    /**
     * Jedis 客户端
     */
    private Jedis jedis;

    public JedisSentinelConnection(RedisNode sentinel) {
        this(sentinel.getHost(), sentinel.getPort());
    }

    public JedisSentinelConnection(String host, int port) {
        this(new Jedis(host, port));
    }

    public JedisSentinelConnection(Jedis jedis) {
        Assert.notNull(jedis, "Cannot created JedisSentinelConnection using 'null' as client.");
        this.jedis = jedis;
        this.init();
    }

    public void failover(NamedNode master) {
        Assert.notNull(master, "Redis node master must not be 'null' for failover.");
        Assert.hasText(master.getName(), "Redis master name must not be 'null' or empty for failover.");
        this.jedis.sentinelFailover(master.getName());
    }

    public List<RedisServer> masters() {
        return JedisConverters.toListOfRedisServer(this.jedis.sentinelMasters());
    }

    public List<RedisServer> slaves(NamedNode master) {
        Assert.notNull(master, "Master node cannot be 'null' when loading slaves.");
        return this.slaves(master.getName());
    }

    public List<RedisServer> slaves(String masterName) {
        Assert.hasText(masterName, "Name of redis master cannot be 'null' or empty when loading slaves.");
        return JedisConverters.toListOfRedisServer(this.jedis.sentinelSlaves(masterName));
    }

    public void remove(NamedNode master) {
        Assert.notNull(master, "Master node cannot be 'null' when trying to remove.");
        this.remove(master.getName());
    }

    public void remove(String masterName) {
        Assert.hasText(masterName, "Name of redis master cannot be 'null' or empty when trying to remove.");
        this.jedis.sentinelRemove(masterName);
    }

    public void monitor(RedisServer server) {
        Assert.notNull(server, "Cannot monitor 'null' server.");
        Assert.hasText(server.getName(), "Name of server to monitor must not be 'null' or empty.");
        Assert.hasText(server.getHost(), "Host must not be 'null' for server to monitor.");
        Assert.notNull(server.getPort(), "Port must not be 'null' for server to monitor.");
        Assert.notNull(server.getQuorum(), "Quorum must not be 'null' for server to monitor.");
        this.jedis.sentinelMonitor(server.getName(), server.getHost(), server.getPort(), server.getQuorum().intValue());
    }

    public void close() throws IOException {
        this.jedis.close();
    }

    private void init() {
        if (!this.jedis.isConnected()) {
            this.doInit(this.jedis);
        }

    }

    protected void doInit(Jedis jedis) {
        jedis.connect();
    }

    public boolean isOpen() {
        return this.jedis.isConnected();
    }
}
