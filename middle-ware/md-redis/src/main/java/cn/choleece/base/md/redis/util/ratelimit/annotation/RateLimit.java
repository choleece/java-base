package cn.choleece.base.md.redis.util.ratelimit.annotation;

import java.lang.annotation.*;

/**
 * @author choleece
 * @Description: 限流注解
 * @Date 2020-04-14 22:45
 **/
@Documented
@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {

    /**
     * 限流模式，默认单击版本
     * @return
     */
    RateLimitMode mode() default RateLimitMode.SINGLE;

    /**
     * 放入令牌的速率，单机版本可以设置
     * @return
     */
    double limit();

    /**
     * 获取令牌超时时间
     * @return
     */
    long timeout() default 0L;
}
