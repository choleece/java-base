package cn.choleece.base.miaosha.common.exception;

import lombok.Data;

/**
 * @author choleece
 * @Description: 系统自定义异常
 * @Date 2020-04-25 23:24
 **/
@Data
public class GlobalException extends RuntimeException {

    private int status;

    public GlobalException(int status, String msg) {
        super(msg);
        this.status = status;
    }
}
