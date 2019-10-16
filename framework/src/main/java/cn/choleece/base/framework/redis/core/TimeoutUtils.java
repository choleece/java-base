package cn.choleece.base.framework.redis.core;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @author choleece
 * @Description: 超时工具
 * @Date 2019-10-15 21:49
 **/
public class TimeoutUtils {

    public TimeoutUtils() {
    }

    public static boolean hasMillis(Duration duration) {
        return duration.toMillis() % 1000L != 0L;
    }

    public static long toSeconds(long timeout, TimeUnit unit) {
        return roundUpIfNecessary(timeout, unit.toSeconds(timeout));
    }

    public static long toMillis(long timeout, TimeUnit unit) {
        return roundUpIfNecessary(timeout, unit.toMillis(timeout));
    }

    private static long roundUpIfNecessary(long timeout, long convertedTimeout) {
        return timeout > 0L && convertedTimeout == 0L ? 1L : convertedTimeout;
    }
}
