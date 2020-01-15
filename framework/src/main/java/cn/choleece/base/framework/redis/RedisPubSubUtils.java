package cn.choleece.base.framework.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-01-15 23:29
 **/
public class RedisPubSubUtils {

    static Jedis jedis = RedisConfig.jedis;

    public static void main(String[] args) {
        jedis.subscribe(new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                super.onMessage(channel, message);

                // 在这里接收消息
                System.out.println("channel: " + channel + " message: " + message);
            }
        }, "redisClient");
    }
}
