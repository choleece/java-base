package cn.choleece.base.md.redis.util.ratelimit;

import java.util.concurrent.TimeUnit;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-04-15 21:27
 **/
public interface CusRateLimit {

    /**
     * 获取令牌
     * @param permits
     * @param timeout
     * @param unit
     * @return
     */
    boolean tryAcquire(int permits, long timeout, TimeUnit unit);

}
