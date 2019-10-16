package cn.choleece.base.framework.redis.connection;

import org.springframework.lang.Nullable;

/**
 * @description: Hyper log log 类型命令
 * @author: sf
 * @time: 2019-10-15 10:31
 */
public interface RedisHyperLogLogCommands {

    @Nullable
    Long pfAdd(byte[] var1, byte[]... var2);

    @Nullable
    Long pfCount(byte[]... var1);

    void pfMerge(byte[] var1, byte[]... var2);
}
