package cn.choleece.base.springboot.retry.exception;

/**
 * @author choleece
 * @Description: one type of exception
 * @Date 2020-09-07 21:56
 **/
public class TwoTypeException extends RuntimeException {

    private String msg;

    public TwoTypeException(String msg) {
        super(msg);
        this.msg = msg;
    }
}
