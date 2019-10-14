package cn.choleece.base.framework.redis.connection;

import java.util.Collection;

/**
 * @description: 订阅
 * @author: sf
 * @time: 2019-10-14 18:02
 */
public interface Subscription {

    void subscribe(byte[]... var1) throws RedisInvalidSubscriptionException;

    void pSubscribe(byte[]... var1) throws RedisInvalidSubscriptionException;

    void unsubscribe();

    void unsubscribe(byte[]... var1);

    void pUnsubscribe();

    void pUnsubscribe(byte[]... var1);

    Collection<byte[]> getChannels();

    Collection<byte[]> getPatterns();

    MessageListener getListener();

    boolean isAlive();

    void close();

}
