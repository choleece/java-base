package cn.choleece.base.framework.redis.connection;

import cn.choleece.base.framework.exception.DataAccessException;
import cn.choleece.base.framework.exception.InvalidDataAccessApiUsageException;
import cn.choleece.base.framework.exception.InvalidDataAccessResourceUsageException;
import cn.choleece.base.framework.redis.RedisSystemException;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: TODO
 * @author: sf
 * @time: 2019-10-14 15:26
 */
public abstract class AbstractRedisConnection implements DefaultedRedisConnection {

    private @Nullable
    RedisSentinelConfiguration sentinelConfiguration;
    private final Map<RedisNode, RedisSentinelConnection> connectionCache = new ConcurrentHashMap<>();

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisConnection#getSentinelCommands()
     */
    @Override
    public RedisSentinelConnection getSentinelConnection() {

        if (!hasRedisSentinelConfigured()) {
            throw new InvalidDataAccessResourceUsageException("No sentinels configured.");
        }

        RedisNode node = selectActiveSentinel();
        RedisSentinelConnection connection = connectionCache.get(node);
        if (connection == null || !connection.isOpen()) {
            connection = getSentinelConnection(node);
            connectionCache.putIfAbsent(node, connection);
        }
        return connection;
    }

    public void setSentinelConfiguration(RedisSentinelConfiguration sentinelConfiguration) {
        this.sentinelConfiguration = sentinelConfiguration;
    }

    public boolean hasRedisSentinelConfigured() {
        return this.sentinelConfiguration != null;
    }

    private RedisNode selectActiveSentinel() {

        Assert.state(hasRedisSentinelConfigured(), "Sentinel configuration missing!");

        for (RedisNode node : this.sentinelConfiguration.getSentinels()) {
            if (isActive(node)) {
                return node;
            }
        }

        throw new InvalidDataAccessApiUsageException("Could not find any active sentinels");
    }

    /**
     * Check if node is active by sending ping.
     *
     * @param node
     * @return
     */
    protected boolean isActive(RedisNode node) {
        return false;
    }

    /**
     * Get {@link RedisSentinelCommands} connected to given node.
     *
     * @param sentinel
     * @return
     */
    protected RedisSentinelConnection getSentinelConnection(RedisNode sentinel) {
        throw new UnsupportedOperationException("Sentinel is not supported by this client.");
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.connection.RedisConnection#close()
     */
    @Override
    public void close() throws DataAccessException {

        if (!connectionCache.isEmpty()) {
            for (RedisNode node : connectionCache.keySet()) {
                RedisSentinelConnection connection = connectionCache.remove(node);
                if (connection.isOpen()) {
                    try {
                        connection.close();
                    } catch (IOException e) {
                        throw new RedisSystemException("Failed to close sentinel connection", e);
                    }
                }
            }
        }
    }
}
