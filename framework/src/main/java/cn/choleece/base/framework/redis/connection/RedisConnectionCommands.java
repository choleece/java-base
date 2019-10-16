package cn.choleece.base.framework.redis.connection;

import org.springframework.lang.Nullable;

/**
 * @description: redis 连接命令，用于判断服务是否存活
 * @author: sf
 * @time: 2019-10-15 10:19
 */
public interface RedisConnectionCommands {

    /**
     * Select the DB with given positive {@code dbIndex}.
     *
     * @param dbIndex the database index.
     * @see <a href="https://redis.io/commands/select">Redis Documentation: SELECT</a>
     */
    void select(int dbIndex);

    /**
     * Returns {@code message} via server roundtrip.
     *
     * @param message the message to echo.
     * @return the message or {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/echo">Redis Documentation: ECHO</a>
     */
    @Nullable
    byte[] echo(byte[] message);

    /**
     * Test connection.
     *
     * @return Server response message - usually {@literal PONG}. {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/ping">Redis Documentation: PING</a>
     */
    @Nullable
    String ping();
}
