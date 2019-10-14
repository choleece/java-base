package cn.choleece.base.framework.exception;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-14 22:43
 **/
public class InvalidDataAccessApiUsageException extends NonTransientDataAccessException {

    public InvalidDataAccessApiUsageException(String msg) {
        super(msg);
    }

    public InvalidDataAccessApiUsageException(String msg, Throwable cause) {
        super(msg, cause);
    }
}