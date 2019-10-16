package cn.choleece.base.framework.redis.connection;

import cn.choleece.base.framework.exception.InvalidDataAccessApiUsageException;
import org.springframework.lang.Nullable;

/**
 * @description: TODO
 * @author: sf
 * @time: 2019-10-16 16:20
 */
public class RedisSubscribedConnectionException extends InvalidDataAccessApiUsageException {

    /**
     * Constructs a new <code>RedisSubscribedConnectionException</code> instance.
     *
     * @param msg
     * @param cause
     */
    public RedisSubscribedConnectionException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }

    /**
     * Constructs a new <code>RedisSubscribedConnectionException</code> instance.
     *
     * @param msg
     */
    public RedisSubscribedConnectionException(String msg) {
        super(msg);
    }
}