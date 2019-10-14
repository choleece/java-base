package cn.choleece.base.framework.exception;

/**
 * @description: TODO
 * @author: sf
 * @time: 2019-10-14 18:10
 */
public class InvalidDataAccessResourceUsageException extends NonTransientDataAccessException {
    public InvalidDataAccessResourceUsageException(String msg) {
        super(msg);
    }

    public InvalidDataAccessResourceUsageException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

