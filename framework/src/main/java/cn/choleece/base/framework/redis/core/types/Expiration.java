package cn.choleece.base.framework.redis.core.types;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-14 22:49
 **/
public class Expiration {

    private long expirationTime;
    private TimeUnit timeUnit;

    protected Expiration(long expirationTime, @Nullable TimeUnit timeUnit) {
        this.expirationTime = expirationTime;
        this.timeUnit = timeUnit != null ? timeUnit : TimeUnit.SECONDS;
    }

    public long getExpirationTimeInMilliseconds() {
        return this.getConverted(TimeUnit.MILLISECONDS);
    }

    public long getExpirationTimeInSeconds() {
        return this.getConverted(TimeUnit.SECONDS);
    }

    public long getExpirationTime() {
        return this.expirationTime;
    }

    public TimeUnit getTimeUnit() {
        return this.timeUnit;
    }

    public long getConverted(TimeUnit targetTimeUnit) {
        Assert.notNull(targetTimeUnit, "TargetTimeUnit must not be null!");
        return targetTimeUnit.convert(this.expirationTime, this.timeUnit);
    }

    public static Expiration seconds(long expirationTime) {
        return new Expiration(expirationTime, TimeUnit.SECONDS);
    }

    public static Expiration milliseconds(long expirationTime) {
        return new Expiration(expirationTime, TimeUnit.MILLISECONDS);
    }

    public static Expiration from(long expirationTime, @Nullable TimeUnit timeUnit) {
        if (!ObjectUtils.nullSafeEquals(timeUnit, TimeUnit.MICROSECONDS) && !ObjectUtils.nullSafeEquals(timeUnit, TimeUnit.NANOSECONDS) && !ObjectUtils.nullSafeEquals(timeUnit, TimeUnit.MILLISECONDS)) {
            return timeUnit != null ? new Expiration(timeUnit.toSeconds(expirationTime), TimeUnit.SECONDS) : new Expiration(expirationTime, TimeUnit.SECONDS);
        } else {
            return new Expiration(timeUnit.toMillis(expirationTime), TimeUnit.MILLISECONDS);
        }
    }

    public static Expiration from(Duration duration) {
        Assert.notNull(duration, "Duration must not be null!");
        return duration.toMillis() % 1000L == 0L ? new Expiration(duration.getSeconds(), TimeUnit.SECONDS) : new Expiration(duration.toMillis(), TimeUnit.MILLISECONDS);
    }

    public static Expiration persistent() {
        return new Expiration(-1L, TimeUnit.SECONDS);
    }

    public boolean isPersistent() {
        return this.expirationTime == -1L;
    }
}
