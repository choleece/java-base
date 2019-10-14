package cn.choleece.base.framework.redis.connection;

import java.util.Collection;

/**
 * @description: Redis 哨兵模式的命令
 * @author: sf
 * @time: 2019-10-14 17:10
 */
public interface RedisSentinelCommands {

    void failover(NamedNode var1);

    Collection<RedisServer> masters();

    Collection<RedisServer> slaves(NamedNode var1);

    void remove(NamedNode var1);

    void monitor(RedisServer var1);

}
