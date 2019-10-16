package cn.choleece.base.framework.redis.connection;

import java.util.List;

/**
 * @description: TODO
 * @author: sf
 * @time: 2019-10-15 10:18
 */
public interface RedisTxCommands {

    void multi();

    List<Object> exec();

    void discard();

    void watch(byte[]... var1);

    void unwatch();
}
