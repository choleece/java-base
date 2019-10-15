package cn.choleece.base.framework.redis.connection;

import org.springframework.lang.Nullable;

/**
 * @description: TODO
 * @author: sf
 * @time: 2019-10-15 10:31
 */
public interface RedisCommands extends RedisKeyCommands, RedisStringCommands, RedisListCommands, RedisSetCommands, RedisZSetCommands, RedisHashCommands, RedisTxCommands, RedisPubSubCommands, RedisConnectionCommands, RedisServerCommands, RedisScriptingCommands, RedisGeoCommands, RedisHyperLogLogCommands {
    @Nullable
    Object execute(String var1, byte[]... var2);
}
