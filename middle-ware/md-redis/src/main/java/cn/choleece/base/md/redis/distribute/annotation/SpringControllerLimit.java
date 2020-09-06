package cn.choleece.base.md.redis.distribute.annotation;

import java.lang.annotation.*;

/**
 * @author choleece
 * @Description: the annotation on controller method
 * @Date 2020-09-06 13:27
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SpringControllerLimit {

    /**
     * Error code
     * @return
     * code
     */
    int errorCode() default 500;

    /**
     * Error Message
     * @return
     * message
     */
    String errorMsg() default "request limited";
}
