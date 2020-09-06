package cn.choleece.base.miaosha.common.service;

import cn.choleece.base.miaosha.common.entity.Stock;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author choleece
 * @since 2020-04-25
 */
public interface IStockService extends IService<Stock> {

    /**
     * 获取剩余库存
     * @param id
     * @return
     */
    int getStockCount(int id) ;

    /**
     * 根据库存 ID 查询库存信息
     * @param id
     * @return
     */
    Stock getStockById(int id) ;

    /**
     * 更新库存信息
     * @param stock
     * return
     */
    int updateStockById(Stock stock);

    /**
     * 乐观锁更新库存
     * @param stock
     * @return
     */
    int updateStockByOptimistic(Stock stock);

}
