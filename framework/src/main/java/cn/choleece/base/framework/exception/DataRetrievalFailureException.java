package cn.choleece.base.framework.exception;

import org.springframework.lang.Nullable;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-14 22:42
 **/
public class DataRetrievalFailureException extends NonTransientDataAccessException {
    public DataRetrievalFailureException(String msg) {
        super(msg);
    }

    public DataRetrievalFailureException(String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
