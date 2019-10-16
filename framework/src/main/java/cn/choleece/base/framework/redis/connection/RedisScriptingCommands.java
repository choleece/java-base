package cn.choleece.base.framework.redis.connection;

import org.springframework.lang.Nullable;

import java.util.List;

/**
 * @description: Redis 脚本执行
 * @author: sf
 * @time: 2019-10-15 10:30
 */
public interface RedisScriptingCommands {

    void scriptFlush();

    void scriptKill();

    @Nullable
    String scriptLoad(byte[] var1);

    @Nullable
    List<Boolean> scriptExists(String... var1);

    @Nullable
    <T> T eval(byte[] var1, ReturnType var2, int var3, byte[]... var4);

    @Nullable
    <T> T evalSha(String var1, ReturnType var2, int var3, byte[]... var4);

    @Nullable
    <T> T evalSha(byte[] var1, ReturnType var2, int var3, byte[]... var4);
}
