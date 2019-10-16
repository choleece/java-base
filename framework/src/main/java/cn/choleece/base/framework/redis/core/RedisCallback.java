package cn.choleece.base.framework.redis.core;

import cn.choleece.base.framework.exception.DataAccessException;
import cn.choleece.base.framework.redis.connection.RedisConnection;
import org.springframework.lang.Nullable;

/**
 * @description: TODO
 * @author: sf
 * @time: 2019-10-14 13:29
 */
public interface RedisCallback<T> {

    @Nullable
    T doInRedis(RedisConnection var1) throws DataAccessException;

}
