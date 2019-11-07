package cn.choleece.base.db.pool.hikari;

/**
 * @description: HikariPool 基本操作
 * @author: choleece
 * @time: 2019-11-07 11:47
 */
public interface HikariPoolMXBean {

    /**
     * Get the number of currently idle connections in the pool
     * 获取当前连接池空闲连接数量
     * @return
     */
    int getIdleConnections();

    /**
     * Get the number of currently active connections in the pool.
     * 获取当前活跃的连接池中活跃的连接数量
     * @return
     */
    int getActiveConnections();

    /**
     * Get the total number of connections currently in the pool
     * 获取当前连接池中所有的连接数量
     * @return
     */
    int getTotalConnections();

    /**
     * the number of threads awaiting a connection from the pool
     * 获取等待从连接池获取一个连接的线程数量
     * @return
     */
    int getThreadsAwaitingConnection();

    /**
     * Evict currently idle connections from the pool, and mark active (in-use) connections for eviction when they are
     * returned to the pool.
     * 清理空闲的连接，并当正在使用的连接回收到连接池时，标记成待清理
     */
    void softEvictConnections();

    /**
     * Suspend the pool.  When the pool is suspended, threads calling {@link # DataSource #getConnection()} will be
     * blocked <i>with no timeout</i> until the pool is resumed via the {@link # resumePool()} method.
     *
     * 挂起连接池，当连接池处于挂起状态时，请求连接池的线程将会被一直阻塞，直至连接池调用了resumePool()方法
     */
    void suspendPool();

    /**
     * Resume the pool.  Enables connection borrowing to resume on a pool that has been suspended via the {@link #suspendPool()} method
     * 唤醒连接池，在调用suspendPool()后调用，表示pool又重新允许线程从pool中获取连接
     */
    void resumePool();

}
