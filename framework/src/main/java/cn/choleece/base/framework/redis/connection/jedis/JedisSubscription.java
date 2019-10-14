package cn.choleece.base.framework.redis.connection.jedis;

import cn.choleece.base.framework.redis.connection.MessageListener;
import org.springframework.lang.Nullable;
import redis.clients.jedis.BinaryJedisPubSub;

/**
 * @description: TODO
 * @author: sf
 * @time: 2019-10-14 18:00
 */
public class JedisSubscription extends AbstractSubscription {

    /**
     * Jedis 二进制发布订阅
     */
    private final BinaryJedisPubSub jedisPubSub;

    JedisSubscription(MessageListener listener, BinaryJedisPubSub jedisPubSub, @Nullable byte[][] channels, @Nullable byte[][] patterns) {
        super(listener, channels, patterns);
        this.jedisPubSub = jedisPubSub;
    }

    protected void doClose() {
        if (!this.getChannels().isEmpty()) {
            this.jedisPubSub.unsubscribe();
        }

        if (!this.getPatterns().isEmpty()) {
            this.jedisPubSub.punsubscribe();
        }
    }

    protected void doPsubscribe(byte[]... patterns) {
        this.jedisPubSub.psubscribe(patterns);
    }

    protected void doPUnsubscribe(boolean all, byte[]... patterns) {
        if (all) {
            this.jedisPubSub.punsubscribe();
        } else {
            this.jedisPubSub.punsubscribe(patterns);
        }
    }

    protected void doSubscribe(byte[]... channels) {
        this.jedisPubSub.subscribe(channels);
    }

    protected void doUnsubscribe(boolean all, byte[]... channels) {
        if (all) {
            this.jedisPubSub.unsubscribe();
        } else {
            this.jedisPubSub.unsubscribe(channels);
        }
    }
}
