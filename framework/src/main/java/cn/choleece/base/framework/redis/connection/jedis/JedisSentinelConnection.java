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
        init();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisSentinelCommands#failover(org.springframework.data.redis.connection.NamedNode)
     */
    @Override
    public void failover(NamedNode master) {

        Assert.notNull(master, "Redis node master must not be 'null' for failover.");
        Assert.hasText(master.getName(), "Redis master name must not be 'null' or empty for failover.");
        jedis.sentinelFailover(master.getName());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisSentinelCommands#masters()
     */
    @Override
    public List<RedisServer> masters() {
        return JedisConverters.toListOfRedisServer(jedis.sentinelMasters());
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisSentinelCommands#slaves(org.springframework.data.redis.connection.NamedNode)
     */
    @Override
    public List<RedisServer> slaves(NamedNode master) {

        Assert.notNull(master, "Master node cannot be 'null' when loading slaves.");
        return slaves(master.getName());
    }

    /**
     * @param masterName
     * @see # RedisSentinelCommands#slaves(NamedNode)
     * @return
     */
    public List<RedisServer> slaves(String masterName) {

        Assert.hasText(masterName, "Name of redis master cannot be 'null' or empty when loading slaves.");
        return JedisConverters.toListOfRedisServer(jedis.sentinelSlaves(masterName));
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisSentinelCommands#remove(org.springframework.data.redis.connection.NamedNode)
     */
    @Override
    public void remove(NamedNode master) {

        Assert.notNull(master, "Master node cannot be 'null' when trying to remove.");
        remove(master.getName());
    }

    /**
     * @param masterName
     * @see # RedisSentinelCommands#remove(NamedNode)
     */
    public void remove(String masterName) {

        Assert.hasText(masterName, "Name of redis master cannot be 'null' or empty when trying to remove.");
        jedis.sentinelRemove(masterName);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisSentinelCommands#monitor(org.springframework.data.redis.connection.RedisServer)
     */
    @Override
    public void monitor(RedisServer server) {

        Assert.notNull(server, "Cannot monitor 'null' server.");
        Assert.hasText(server.getName(), "Name of server to monitor must not be 'null' or empty.");
        Assert.hasText(server.getHost(), "Host must not be 'null' for server to monitor.");
        Assert.notNull(server.getPort(), "Port must not be 'null' for server to monitor.");
        Assert.notNull(server.getQuorum(), "Quorum must not be 'null' for server to monitor.");
        jedis.sentinelMonitor(server.getName(), server.getHost(), server.getPort().intValue(),
                server.getQuorum().intValue());
    }

    /*
     * (non-Javadoc)
     * @see java.io.Closeable#close()
     */
    @Override
    public void close() throws IOException {
        jedis.close();
    }

    private void init() {
        if (!jedis.isConnected()) {
            doInit(jedis);
        }
    }

    /**
     * Do what ever is required to establish the connection to redis.
     *
     * @param jedis
     */
    protected void doInit(Jedis jedis) {
        jedis.connect();
    }

    @Override
    public boolean isOpen() {
        return jedis.isConnected();
    }
}
