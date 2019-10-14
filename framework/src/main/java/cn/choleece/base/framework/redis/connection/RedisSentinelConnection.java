package cn.choleece.base.framework.redis.connection;

import java.io.Closeable;

/**
 * @description: TODO
 * @author: sf
 * @time: 2019-10-14 17:09
 */
public interface RedisSentinelConnection extends RedisSentinelCommands, Closeable {

    boolean isOpen();

}
