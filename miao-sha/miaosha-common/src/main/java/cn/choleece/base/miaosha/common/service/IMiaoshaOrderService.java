package cn.choleece.base.miaosha.common.service;

import cn.choleece.base.miaosha.common.controller.model.CreateOrderModel;
import cn.choleece.base.miaosha.common.entity.MiaoshaOrder;
import cn.choleece.base.miaosha.common.util.R;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author choleece
 * @since 2020-04-25
 */
public interface IMiaoshaOrderService extends IService<MiaoshaOrder> {

    /**
     * 创建订单
     * @param createOrderModel
     * @return
     */
    R createOrder(CreateOrderModel createOrderModel);

    /**
     * 利用数据库悲观锁
     * @param createOrderModel
     * @return
     */
    R createOrderWithPessimisticDbLock(CreateOrderModel createOrderModel);

    /**
     * 利用数据库乐观锁
     * @param createOrderModel
     * @return
     * @throws Exception
     */
    R createOrderWithPessimisticDbLuckyLock(CreateOrderModel createOrderModel) throws Exception;

    /**
     * 通过从缓存里获取数据，进行判断
     * @param createOrderModel
     * @return
     */
    R createOrderWithRedis(CreateOrderModel createOrderModel);

}
