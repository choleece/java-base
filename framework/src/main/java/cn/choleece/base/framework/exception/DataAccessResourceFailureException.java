package cn.choleece.base.framework.exception;

import org.springframework.lang.Nullable;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-14 22:44
 **/
public class DataAccessResourceFailureException extends NonTransientDataAccessResourceException {
    public DataAccessResourceFailureException(String msg) {
        super(msg);
    }

    public DataAccessResourceFailureException(String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
