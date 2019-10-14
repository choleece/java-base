package cn.choleece.base.framework.redis.connection.convert;

import org.springframework.lang.Nullable;

/**
 * @description: TODO
 * @author: sf
 * @time: 2019-10-14 18:16
 */
@FunctionalInterface
public interface Converter<S, T> {

    @Nullable
    T convert(S var1);
}
