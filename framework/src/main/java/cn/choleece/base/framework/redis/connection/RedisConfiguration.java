package cn.choleece.base.framework.redis.connection;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Set;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

/**
 * @author choleece
 * @Description: Redis 配置抽象
 * @Date 2019-10-13 21:59
 **/
public interface RedisConfiguration {

    /**
     * 在interface 中使用 static 和 default， 是可以允许有方法体的，属于Java 8的新特性
     * 具体例子参考: https://www.cnblogs.com/yanggb/p/10593052.html
     * @param other
     * @return
     */
    default Integer getDatabaseOrElse(Supplier<Integer> other) {
        return getDatabaseOrElse(this, other);
    }

    default RedisPassword getPasswordOrElse(Supplier<RedisPassword> other) {
        return getPasswordOrElse(this, other);
    }

    static boolean isPasswordAware(@Nullable RedisConfiguration configuration) {
        return configuration instanceof RedisConfiguration.WithPassword;
    }

    static boolean isDatabaseIndexAware(@Nullable RedisConfiguration configuration) {
        return configuration instanceof RedisConfiguration.WithDatabaseIndex;
    }

    static boolean isSentinelConfiguration(@Nullable RedisConfiguration configuration) {
        return configuration instanceof RedisConfiguration.SentinelConfiguration;
    }

    static boolean isHostAndPortAware(@Nullable RedisConfiguration configuration) {
        return configuration instanceof RedisConfiguration.WithHostAndPort;
    }

    static boolean isClusterConfiguration(@Nullable RedisConfiguration configuration) {
        return configuration instanceof RedisConfiguration.ClusterConfiguration;
    }

    static boolean isStaticMasterReplicaConfiguration(@Nullable RedisConfiguration configuration) {
        return configuration instanceof RedisConfiguration.StaticMasterReplicaConfiguration;
    }

    static boolean isDomainSocketConfiguration(@Nullable RedisConfiguration configuration) {
        return configuration instanceof RedisConfiguration.DomainSocketConfiguration;
    }

    static Integer getDatabaseOrElse(@Nullable RedisConfiguration configuration, Supplier<Integer> other) {
        Assert.notNull(other, "Other must not be null!");
        return isDatabaseIndexAware(configuration) ? ((RedisConfiguration.WithDatabaseIndex)configuration).getDatabase() : (Integer)other.get();
    }

    static RedisPassword getPasswordOrElse(@Nullable RedisConfiguration configuration, Supplier<RedisPassword> other) {
        Assert.notNull(other, "Other must not be null!");
        return isPasswordAware(configuration) ? ((RedisConfiguration.WithPassword)configuration).getPassword() : (RedisPassword)other.get();
    }

    static int getPortOrElse(@Nullable RedisConfiguration configuration, IntSupplier other) {
        Assert.notNull(other, "Other must not be null!");
        return isHostAndPortAware(configuration) ? ((RedisConfiguration.WithHostAndPort)configuration).getPort() : other.getAsInt();
    }

    static String getHostOrElse(@Nullable RedisConfiguration configuration, Supplier<String> other) {
        Assert.notNull(other, "Other must not be null!");
        return isHostAndPortAware(configuration) ? ((RedisConfiguration.WithHostAndPort)configuration).getHostName() : (String)other.get();
    }

    interface DomainSocketConfiguration extends RedisConfiguration.WithDomainSocket, RedisConfiguration.WithDatabaseIndex, RedisConfiguration.WithPassword {
    }

    interface StaticMasterReplicaConfiguration extends RedisConfiguration.WithDatabaseIndex, RedisConfiguration.WithPassword {
        List<RedisStandaloneConfiguration> getNodes();
    }

    interface ClusterConfiguration extends RedisConfiguration.WithPassword {
        Set<RedisNode> getClusterNodes();

        @Nullable
        Integer getMaxRedirects();
    }

    interface SentinelConfiguration extends RedisConfiguration.WithDatabaseIndex, RedisConfiguration.WithPassword {
        default void setMaster(String name) {
            Assert.notNull(name, "Name of sentinel master must not be null.");
            this.setMaster(() -> name);
        }

        /**
         * 设置主节点
         * @param var1
         */
        void setMaster(NamedNode var1);

        @Nullable
        NamedNode getMaster();

        Set<RedisNode> getSentinels();
    }

    interface WithDomainSocket {
        void setSocket(String var1);

        String getSocket();
    }

    interface WithHostAndPort {
        void setHostName(String var1);

        String getHostName();

        void setPort(int var1);

        int getPort();
    }

    interface WithDatabaseIndex {
        void setDatabase(int var1);

        int getDatabase();
    }

    interface WithPassword {
        default void setPassword(@Nullable String password) {
            this.setPassword(RedisPassword.of(password));
        }

        default void setPassword(@Nullable char[] password) {
            this.setPassword(RedisPassword.of(password));
        }

        void setPassword(RedisPassword var1);

        RedisPassword getPassword();
    }
}

