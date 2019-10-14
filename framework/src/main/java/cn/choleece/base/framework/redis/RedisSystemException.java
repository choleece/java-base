package cn.choleece.base.framework.redis;

import cn.choleece.base.framework.exception.UncategorizedDataAccessException;

/**
 * @description: TODO
 * @author: sf
 * @time: 2019-10-14 17:37
 */
public class RedisSystemException extends UncategorizedDataAccessException {

    public RedisSystemException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
