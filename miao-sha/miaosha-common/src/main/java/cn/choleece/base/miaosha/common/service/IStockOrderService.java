package cn.choleece.base.miaosha.common.service;

import cn.choleece.base.miaosha.common.entity.StockOrder;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author choleece
 * @since 2020-04-25
 */
public interface IStockOrderService extends IService<StockOrder> {

    /**
     * 创建订单
     * @param sid
     *  库存ID
     * @return
     *  订单ID
     */
    int createWrongOrder(int sid) throws Exception;


    /**
     * 创建订单 乐观锁
     * @param sid
     * @return
     * @throws Exception
     */
    int createOptimisticOrder(int sid) throws Exception;


    /**
     * 创建订单 乐观锁，库存查 Redis 减小 DB 压力。
     * @param sid
     * @return
     * @throws Exception
     */
    int createOptimisticOrderUseRedis(int sid) throws Exception ;


    /**
     * 创建订单 乐观锁，库存查 Redis 减小 DB 压力。
     * 利用 Kafka 异步写订单
     * @param sid
     * @return
     * @throws Exception
     */
    void createOptimisticOrderUseRedisAndKafka(int sid) throws Exception ;

}
