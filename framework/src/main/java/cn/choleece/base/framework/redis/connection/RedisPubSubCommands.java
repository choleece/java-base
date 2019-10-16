package cn.choleece.base.framework.redis.connection;

import org.springframework.lang.Nullable;

/**
 * @description: 发布订阅
 * @author: sf
 * @time: 2019-10-15 10:19
 */
public interface RedisPubSubCommands {

    boolean isSubscribed();

    @Nullable
    Subscription getSubscription();

    @Nullable
    Long publish(byte[] var1, byte[] var2);

    void subscribe(MessageListener var1, byte[]... var2);

    void pSubscribe(MessageListener var1, byte[]... var2);
}
