package cn.choleece.base.framework.redis.connection;

import org.springframework.lang.Nullable;

/**
 * @description: TODO
 * @author: sf
 * @time: 2019-10-14 18:03
 */
public interface MessageListener {

    /**
     * Callback for processing received objects through Redis.
     *
     * @param message message must not be {@literal null}.
     * @param pattern pattern matching the channel (if specified) - can be {@literal null}.
     */
    void onMessage(Message message, @Nullable byte[] pattern);

}
