package cn.choleece.base.framework.redis.core;

import cn.choleece.base.framework.exception.DataAccessException;
import org.springframework.lang.Nullable;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-15 22:51
 **/
public interface SessionCallback<T> {
    @Nullable
    <K, V> T execute(RedisOperations<K, V> var1) throws DataAccessException;
}
