package cn.choleece.base.db.pool.hikari;

/**
 * @description: TODO
 * @author: choleece
 * @time: 2019-11-07 13:21
 */
public interface HikariConfigMXBean {

    /**
     * Get the maximum number of milliseconds that a client will wait for a connection from the pool. If this
     * time is exceeded without a connection becoming available, a SQLException will be thrown from
     * 获取连接超时时间
     * @return
     */
    long getConnectionTimeout();

    void setConnectionTimeout(long connectionTimeoutMs);

    /**
     * Sets the maximum number of milliseconds that the pool will wait for a connection to be validated as alive
     * 设置最大验证时间 验证连接是否可用
     * @return
     */
    long getValidationTimeout();

    void setValidationTimeout(long validationTimeoutMs);

    /**
     * 关于idle time 可以参考：https://juejin.im/post/5a77c15b6fb9a063606eb473
     *
     * 当这个时间为0时，表示不会清理空闲连接，否则，当空闲连接数量 > minIdleConnection时，就判断空闲时间是否超过这个值，如果超过，会执行清理
     * @return
     */
    long getIdleTimeout();

    void setIdleTimeout(long idleTimeoutMs);

    /**
     * This property controls the amount of time that a connection can be out of the pool before a message is
     * logged indicating a possible connection leak. A value of 0 means leak detection is disabled
     *
     * 用来设置连接被占用的超时时间，单位为毫秒，默认为0，表示禁用连接泄露检测
     *
     * 具体可以参考：https://www.jianshu.com/p/3f6f17402551
     * @return
     */
    long getLeakDetectionThreshold();

    void setLeakDetectionThreshold(long leakDetectionThresholdMs);

    /**
     * This property controls the maximum lifetime of a connection in the pool. When a connection reaches this
     * timeout, even if recently used, it will be retired from the pool. An in-use connection will never be
     * retired, only when it is idle will it be removed
     *
     * 一个连接最大当生命周期时长（只有当空闲的时候才会被清理）
     *
     * 我们在看Mysql数据库的时候，可以了解到，一个连接在使用的时候，会占用一些内存资源，使用时间越长，申请的资源会越多，所以适当的断开连接重连会释放一些资源，还有一个情况是此连接是一个大查询，也会占用很多资源
     * Mysql 默认连接时长为8h,也可以设置connection reset重置连接，将连接重置到刚建立连接时候的状态
     * @return
     */
    long getMaxLifetime();

    void setMaxLifetime(long maxLifetimeMs);

    /**
     * The property controls the minimum number of idle connections that HikariCP tries to maintain in the pool,
     * including both idle and in-use connections. If the idle connections dip below this value, HikariCP will
     * make a best effort to restore them quickly and efficiently.
     *
     * 最小的空闲连接数量
     * @return
     */
    int getMinimumIdle();

    void setMinimumIdle(int minIdle);

    /**
     * The property controls the maximum size that the pool is allowed to reach, including both idle and in-use
     * connections. Basically this value will determine the maximum number of actual connections to the database
     * backend.
     *
     * When the pool reaches this size, and no idle connections are available, calls to getConnection() will
     * block for up to connectionTimeout milliseconds before timing out
     *
     * 连接池的最大数量，这个连接包括空闲的数量 + 正在运行的数量，当然，最大连接数受限于数据库所支持的最大连接数
     * @return
     */
    int getMaximumPoolSize();

    void setMaximumPoolSize(int maxPoolSize);

    void setPassword(String password);

    void setUsername(String username);

    /**
     * The name of the connection pool
     *
     * 连接池的名字
     * @return
     */
    String getPoolName();

    /**
     * Get the default catalog name to be set on connections
     *
     * 数据库名
     * @return
     */
    String getCatalog();

    void setCatalog(String catalog);
}
