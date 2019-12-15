package cn.choleece.base.miaosha.limiter;

import com.google.common.util.concurrent.RateLimiter;

/**
 * @author choleece
 * @Description: 基于Guava实现限流
 * @Date 2019-12-15 23:34
 **/
public class GuavaLimiter {

    public static void test() {

        /**
         * 创建一个限流器，每秒产生两个token放入桶中，
         */
        RateLimiter r = RateLimiter.create(2);

        for (int i = 0; i < 10000000; i++) {

            // 这里会被阻塞，取不到内容会被阻塞
            double acquire = r.acquire();
            System.out.println("acquire: " + acquire);
        }
    }

    public static void main(String[] args) {
        test();
    }
}
