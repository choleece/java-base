package cn.choleece.base.md.redis.util.ratelimit.annotation;

/**
 * @author choleece
 * @Description: 限流模式
 * @Date 2020-04-14 22:46
 **/
public enum RateLimitMode {

    /**
     * 分布式限流
     */
    DISTRIBUTE,

    /**
     * 单机限流
     */
    SINGLE
}
