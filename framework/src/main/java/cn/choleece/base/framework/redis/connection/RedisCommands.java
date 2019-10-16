package cn.choleece.base.framework.redis.connection;

import org.springframework.lang.Nullable;

/**
 * @description: Redis 命令抽象
 * @author: sf
 * @time: 2019-10-15 10:31
 *
 * 暂时只考虑String 类型的命令
 * RedisListCommands, RedisSetCommands, RedisZSetCommands, RedisHashCommands, RedisTxCommands, RedisScriptingCommands, RedisGeoCommands, RedisHyperLogLogCommands
 */
public interface RedisCommands extends RedisKeyCommands, RedisStringCommands, RedisPubSubCommands, RedisConnectionCommands, RedisServerCommands {
    /**
     * 'Native' or 'raw' execution of the given command along-side the given arguments. The command is executed as is,
     * with as little 'interpretation' as possible - it is up to the caller to take care of any processing of arguments or
     * the result.
     *
     * @param command Command to execute. must not be {@literal null}.
     * @param args Possible command arguments (may be empty).
     * @return execution result. Can be {@literal null}.
     */
    @Nullable
    Object execute(String command, byte[]... args);
}
