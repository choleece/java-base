package cn.choleece.base.framework.redis.connection;

import cn.choleece.base.framework.exception.InvalidDataAccessResourceUsageException;

/**
 * @description: TODO
 * @author: sf
 * @time: 2019-10-14 18:10
 */
public class RedisInvalidSubscriptionException extends InvalidDataAccessResourceUsageException {
    public RedisInvalidSubscriptionException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public RedisInvalidSubscriptionException(String msg) {
        super(msg);
    }
}
