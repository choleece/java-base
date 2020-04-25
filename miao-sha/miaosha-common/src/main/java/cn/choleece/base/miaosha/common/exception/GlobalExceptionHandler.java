package cn.choleece.base.miaosha.common.exception;

import cn.choleece.base.miaosha.common.util.R;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author choleece
 * @Description: 统一异常处理
 * @Date 2020-04-25 23:36
 **/
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public String defaultRestException(Exception ex) {
        ex.printStackTrace();
        return R.error();
    }
}
