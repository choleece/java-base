package cn.choleece.base.framework.redis.connection;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author choleece
 * @Description: Redis 密码配置
 * @Date 2019-10-13 22:01
 **/
public class RedisPassword {

    private static final RedisPassword NONE = new RedisPassword(new char[0]);

    /**
     * final 修饰，必须初始化
     */
    private final char[] thePassword;

    public static RedisPassword of(@Nullable String passwordAsString) {
        return (RedisPassword)Optional.ofNullable(passwordAsString).filter(StringUtils::hasText)
                .map(it -> new RedisPassword(it.toCharArray()))
                .orElseGet(RedisPassword::none);
    }

    public static RedisPassword of(@Nullable char[] passwordAsChars) {
        return (RedisPassword)Optional.ofNullable(passwordAsChars).filter(it -> !ObjectUtils.isEmpty(passwordAsChars))
                .map(it -> new RedisPassword(Arrays.copyOf(it, it.length)))
                .orElseGet(RedisPassword::none);
    }

    public static RedisPassword none() {
        return NONE;
    }

    public boolean isPresent() {
        return !ObjectUtils.isEmpty(this.thePassword);
    }

    public char[] get() throws NoSuchElementException {
        if (this.isPresent()) {
            return Arrays.copyOf(this.thePassword, this.thePassword.length);
        } else {
            throw new NoSuchElementException("No password present.");
        }
    }

    public <R> Optional<R> map(Function<char[], R> mapper) {
        Assert.notNull(mapper, "Mapper function must not be null");
        return this.toOptional().map(mapper);
    }

    public Optional<char[]> toOptional() {
        return this.isPresent() ? Optional.of(this.get()) : Optional.empty();
    }

    @Override
    public String toString() {
        return String.format("%s[%s]", this.getClass().getSimpleName(), this.isPresent() ? "*****" : "<none>");
    }

    private RedisPassword(char[] thePassword) {
        this.thePassword = thePassword;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof RedisPassword)) {
            return false;
        } else {
            RedisPassword other = (RedisPassword)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                return Arrays.equals(this.thePassword, other.thePassword);
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof RedisPassword;
    }

    @Override
    public int hashCode() {
        int result = 1;
        return result * 59 + Arrays.hashCode(this.thePassword);
    }
}
