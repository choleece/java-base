package cn.choleece.base.framework.redis;

import cn.choleece.base.framework.exception.DataRetrievalFailureException;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-14 22:42
 **/
public class TooManyClusterRedirectionsException extends DataRetrievalFailureException {
    private static final long serialVersionUID = -2818933672669154328L;

    public TooManyClusterRedirectionsException(String msg) {
        super(msg);
    }

    public TooManyClusterRedirectionsException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
