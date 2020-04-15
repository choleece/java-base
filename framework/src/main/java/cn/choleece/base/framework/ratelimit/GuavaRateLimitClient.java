package cn.choleece.base.framework.ratelimit;

import com.google.common.util.concurrent.RateLimiter;

/**
 * @author choleece
 * @Description: guava 限流器测试
 * @Date 2020-04-12 16:59
 **/
public class GuavaRateLimitClient {

    public static void main(String[] args) {
        RateLimiter rateLimiter = RateLimiter.create(90);

        for (int i = 1; i < 10; i += 2) {
            double waitTime = rateLimiter.acquire();

            System.out.println("cutTime=" + System.currentTimeMillis() + " acq:" + i + " waitTime:" + waitTime);
        }
    }

}
