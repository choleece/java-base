package cn.choleece.base.springboot.annotation;

import java.lang.annotation.*;

/**
 *
 * @author choleece
 * @date 2020-09-12
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentUser {

    /**
     * 当前用户在request中的名字
     *
     * @return 通过resolver 赋值的对象
     */
    String value() default "user";
}
