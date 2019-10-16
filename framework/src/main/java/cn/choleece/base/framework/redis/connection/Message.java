package cn.choleece.base.framework.redis.connection;

import java.io.Serializable;

/**
 * @description: 消息载体
 * @author: sf
 * @time: 2019-10-14 18:04
 */
public interface Message extends Serializable {

    /**
     * Returns the body (or the payload) of the message.
     *
     * @return message body. Never {@literal null}.
     */
    byte[] getBody();

    /**
     * Returns the channel associated with the message.
     *
     * @return message channel. Never {@literal null}.
     */
    byte[] getChannel();
}
