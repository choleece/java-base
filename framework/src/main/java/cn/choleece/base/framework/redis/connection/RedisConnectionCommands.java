package cn.choleece.base.framework.redis.connection;

import org.springframework.lang.Nullable;

/**
 * @description: redis 连接命令，用于判断服务是否存活
 * @author: sf
 * @time: 2019-10-15 10:19
 */
public interface RedisConnectionCommands {

    void select(int var1);

    @Nullable
    byte[] echo(byte[] var1);

    @Nullable
    String ping();
}
