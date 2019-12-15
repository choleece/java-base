package cn.choleece.base.miaosha.limiter;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-12-11 22:59
 **/
public class CountLimiter {

    private static AtomicInteger limiter = new AtomicInteger(0);

    private static long currentSecond = 0;

    private static int MAX_TPS = 1000;

    /**
     * 1s 内访问的次数
     * @return
     */
    public static int calculateTps() {
        if (currentSecond == System.currentTimeMillis() / 1000) {
            return limiter.getAndIncrement();
        } else {
            limiter.set(0);
            currentSecond = System.currentTimeMillis() / 1000;
            return 1;
        }
    }

    /**
     * 判断是否超过最大TPS
     * @return
     */
    public static boolean overReq() {
        return MAX_TPS >= calculateTps();
    }
}
