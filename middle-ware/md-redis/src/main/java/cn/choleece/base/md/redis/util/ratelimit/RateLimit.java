package cn.choleece.base.md.redis.util.ratelimit;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-04-28 08:01
 **/
public interface RateLimit {

    /**
     * 限流，是否获取得权限，可以访问
     * @param key 限流的key，可以针对特定的接口
     * @param limit 每秒种请求的次数
     * @return
     */
    boolean acquire(String key, long limit);

}
