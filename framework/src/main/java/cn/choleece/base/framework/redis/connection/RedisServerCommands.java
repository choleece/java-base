package cn.choleece.base.framework.redis.connection;

import cn.choleece.base.framework.redis.core.types.RedisClientInfo;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Properties;

/**
 * @description: Redis 服务器命令
 * @author: sf
 * @time: 2019-10-15 10:20
 */
public interface RedisServerCommands {

    /** @deprecated */
    @Deprecated
    default void bgWriteAof() {
        this.bgReWriteAof();
    }

    void bgReWriteAof();

    void bgSave();

    @Nullable
    Long lastSave();

    void save();

    @Nullable
    Long dbSize();

    void flushDb();

    void flushAll();

    @Nullable
    Properties info();

    @Nullable
    Properties info(String var1);

    void shutdown();

    void shutdown(RedisServerCommands.ShutdownOption var1);

    @Nullable
    Properties getConfig(String var1);

    void setConfig(String var1, String var2);

    void resetConfigStats();

    @Nullable
    Long time();

    void killClient(String var1, int var2);

    void setClientName(byte[] var1);

    @Nullable
    String getClientName();

    @Nullable
    List<RedisClientInfo> getClientList();

    void slaveOf(String var1, int var2);

    void slaveOfNoOne();

    void migrate(byte[] var1, RedisNode var2, int var3, @Nullable RedisServerCommands.MigrateOption var4);

    void migrate(byte[] var1, RedisNode var2, int var3, @Nullable RedisServerCommands.MigrateOption var4, long var5);

    public static enum MigrateOption {
        COPY,
        REPLACE;

        private MigrateOption() {
        }
    }

    public static enum ShutdownOption {
        SAVE,
        NOSAVE;

        private ShutdownOption() {
        }
    }
}
