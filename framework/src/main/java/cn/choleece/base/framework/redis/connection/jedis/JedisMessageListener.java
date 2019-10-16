package cn.choleece.base.framework.redis.connection.jedis;

import cn.choleece.base.framework.redis.connection.DefaultMessage;
import cn.choleece.base.framework.redis.connection.MessageListener;
import org.springframework.util.Assert;
import redis.clients.jedis.BinaryJedisPubSub;

/**
 * @description: TODO
 * @author: sf
 * @time: 2019-10-16 15:55
 */
public class JedisMessageListener extends BinaryJedisPubSub {

    private final MessageListener listener;

    JedisMessageListener(MessageListener listener) {
        Assert.notNull(listener, "message listener is required");
        this.listener = listener;
    }

    public void onMessage(byte[] channel, byte[] message) {
        listener.onMessage(new DefaultMessage(channel, message), null);
    }

    public void onPMessage(byte[] pattern, byte[] channel, byte[] message) {
        listener.onMessage(new DefaultMessage(channel, message), pattern);
    }

    public void onPSubscribe(byte[] pattern, int subscribedChannels) {
        // no-op
    }

    public void onPUnsubscribe(byte[] pattern, int subscribedChannels) {
        // no-op
    }

    public void onSubscribe(byte[] channel, int subscribedChannels) {
        // no-op
    }

    public void onUnsubscribe(byte[] channel, int subscribedChannels) {
        // no-op
    }
}
