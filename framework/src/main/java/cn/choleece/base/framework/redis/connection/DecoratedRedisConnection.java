package cn.choleece.base.framework.redis.connection;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-15 23:04
 **/
public interface DecoratedRedisConnection {
    RedisConnection getDelegate();
}
