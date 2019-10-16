package cn.choleece.base.framework.redis.connection;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * @description: TODO
 * @author: sf
 * @time: 2019-10-16 15:56
 */
public class DefaultMessage implements Message {

    private final byte[] channel;
    private final byte[] body;
    private @Nullable
    String toString;

    public DefaultMessage(byte[] channel, byte[] body) {

        Assert.notNull(channel, "Channel must not be null!");
        Assert.notNull(body, "Body must not be null!");

        this.body = body;
        this.channel = channel;
    }

    /**
     * @return
     */
    public byte[] getChannel() {
        return channel.clone();
    }

    public byte[] getBody() {
        return body.clone();
    }

    @Override
    public String toString() {

        if (toString == null) {
            toString = new String(body);
        }
        return toString;
    }
}
