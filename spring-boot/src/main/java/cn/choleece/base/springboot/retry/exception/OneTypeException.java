package cn.choleece.base.springboot.retry.exception;

/**
 * @author choleece
 * @Description: one type of exception
 * @Date 2020-09-07 21:56
 **/
public class OneTypeException extends RuntimeException {

    private String msg;

    public OneTypeException(String msg) {
        super(msg);
        this.msg = msg;
    }
}
