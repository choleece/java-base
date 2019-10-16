package cn.choleece.base.framework.redis.connection;

import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * @description: Redis 连接
 * @author: sf
 * @time: 2019-10-14 15:31
 */
public interface RedisConnection extends RedisCommands {

    default RedisGeoCommands geoCommands() {
        return this;
    }

    default RedisHashCommands hashCommands() {
        return this;
    }

    default RedisHyperLogLogCommands hyperLogLogCommands() {
        return this;
    }

    default RedisKeyCommands keyCommands() {
        return this;
    }

    default RedisListCommands listCommands() {
        return this;
    }

    default RedisSetCommands setCommands() {
        return this;
    }

    default RedisScriptingCommands scriptingCommands() {
        return this;
    }

    default RedisServerCommands serverCommands() {
        return this;
    }

    default RedisStringCommands stringCommands() {
        return this;
    }

    default RedisZSetCommands zSetCommands() {
        return this;
    }

    void close() throws DataAccessException;

    boolean isClosed();

    Object getNativeConnection();

    boolean isQueueing();

    boolean isPipelined();

    void openPipeline();

    List<Object> closePipeline() throws RedisPipelineException;

    RedisSentinelConnection getSentinelConnection();
}
