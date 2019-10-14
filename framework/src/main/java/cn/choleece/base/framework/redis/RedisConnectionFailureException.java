package cn.choleece.base.framework.redis;

import cn.choleece.base.framework.exception.DataAccessResourceFailureException;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-14 22:45
 **/
public class RedisConnectionFailureException extends DataAccessResourceFailureException {

    public RedisConnectionFailureException(String msg) {
        super(msg);
    }

    public RedisConnectionFailureException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
