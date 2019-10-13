package cn.choleece.base.framework.redis.connection;

import org.springframework.util.Assert;

/**
 * @author choleece
 * @Description: 单节点Redis 配置
 * @Date 2019-10-13 22:39
 **/
public class RedisStandaloneConfiguration implements RedisConfiguration, RedisConfiguration.WithHostAndPort, RedisConfiguration.WithPassword, RedisConfiguration.WithDatabaseIndex {

    private static final String DEFAULT_HOST = "localhost";

    private static final int DEFAULT_PORT = 6379;

    private String hostName;

    private int port;

    private int database;

    private RedisPassword password;

    public RedisStandaloneConfiguration() {
        this.hostName = "localhost";
        this.port = 6379;
        this.password = RedisPassword.none();
    }

    public RedisStandaloneConfiguration(String hostName) {
        this(hostName, 6379);
    }

    public RedisStandaloneConfiguration(String hostName, int port) {
        this.hostName = "localhost";
        this.port = 6379;
        this.password = RedisPassword.none();

        Assert.hasText(hostName, "Host name must not be null or empty!");
        Assert.isTrue(port >= 1 && port <= 65535, () -> String.format("Port %d must be a valid TCP port in the range between 1-65535!", port));

        this.hostName = hostName;
        this.port = port;
    }

    @Override
    public String getHostName() {
        return hostName;
    }

    @Override
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public int getDatabase() {
        return database;
    }

    @Override
    public void setDatabase(int database) {

        Assert.isTrue(database >= 0, () -> String.format("Invalid DB index '%s' (a position index required)", database));

        this.database = database;
    }

    @Override
    public RedisPassword getPassword() {
        return password;
    }

    @Override
    public void setPassword(RedisPassword password) {
        Assert.notNull(password, "RedisPassword must not be null!");
        this.password = password;
    }
}
