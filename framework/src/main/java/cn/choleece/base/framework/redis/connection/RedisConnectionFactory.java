package cn.choleece.base.framework.redis.connection;

import cn.choleece.base.framework.redis.support.PersistenceExceptionTranslator;

/**
 * @description: Redis 连接工厂
 * @author: sf
 * @time: 2019-10-14 15:39
 */
public interface RedisConnectionFactory extends PersistenceExceptionTranslator {

    /**
     * Redis 连接 用于standalone模式
     * @return
     */
    RedisConnection getConnection();

    /**
     * Redis 集群模式连接 暂时先不考虑
     * @return
     */
//    RedisClusterConnection getClusterConnection();

    boolean getConvertPipelineAndTxResults();

    /**
     * Redis 哨兵模式连接 暂时先不考虑
     * @return
     */
//    RedisSentinelConnection getSentinelConnection();
}
