package cn.choleece.base.miaosha.common.util.ratelimit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Collections;
import java.util.List;

/**
 * @author choleece
 * @Description: redis lua限流
 * @Date 2020-04-28 08:01
 **/
@Component
public class RedisRateLimit implements RateLimit {
    @Autowired
    private JedisPool jedisPool;

    private final String RATE_LIMIT_LUA_SCRIPT = "local key = KEYS[1] " +
            "local limit = tonumber(ARGV[1]) " +
            "local current = tonumber(redis.call('get', key) or '0') " +
            "if current + 1 > limit then " +
            "    return 0 " +
            "else " +
            "    redis.call('INCRBY', key, '1') " +
            "    redis.call('expire', key, '2') " +
            "    return 1 " +
            "end";

    @Override
    public boolean acquire(String key, long limit) {
        Jedis jedis = jedisPool.getResource();
        try {
            // 取1s内访问的次数
            String luaKey = key + System.currentTimeMillis() / 1000;
            List<String> keys = Collections.singletonList(luaKey);
            List<String> args = Collections.singletonList(String.valueOf(limit));
            Long result = (Long) jedis.evalsha(jedis.scriptLoad(RATE_LIMIT_LUA_SCRIPT), keys, args);
            System.out.println(result);
            return result == 1;
        } finally {
            jedis.close();
        }
    }
}
