package cn.choleece.base.framework.redis.connection;

import org.springframework.lang.Nullable;

/**
 * @description: TODO
 * @author: sf
 * @time: 2019-10-14 18:03
 */
public interface MessageListener {

    void onMessage(Message var1, @Nullable byte[] var2);

}
