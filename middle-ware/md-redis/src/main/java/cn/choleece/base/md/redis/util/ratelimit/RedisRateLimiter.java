package cn.choleece.base.md.redis.util.ratelimit;

import cn.choleece.base.md.redis.command.RedisConfig;
import redis.clients.jedis.Jedis;

/**
 * @author choleece
 * @Description: redis 分布式限流器
 * @Date 2020-04-10 23:21
 **/
public class RedisRateLimiter {

    Jedis jedis = RedisConfig.jedis;

}
