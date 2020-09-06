package cn.choleece.base.md.redis.util.ratelimit;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.TimeUnit;

/**
 * @author choleece
 * @Description: Guava 限流器
 * @Date 2020-04-15 21:29
 **/
public class GuavaRateLimit implements CusRateLimit {

    RateLimiter rateLimiter = RateLimiter.create(90);

    @Override
    public boolean tryAcquire(int permits, long timeout, TimeUnit unit) {
        return rateLimiter.tryAcquire(permits, timeout, unit);
    }
}
