package cn.choleece.base.framework.exception;

import org.springframework.lang.Nullable;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-14 22:45
 **/
public class NonTransientDataAccessResourceException extends NonTransientDataAccessException {
    public NonTransientDataAccessResourceException(String msg) {
        super(msg);
    }

    public NonTransientDataAccessResourceException(String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
