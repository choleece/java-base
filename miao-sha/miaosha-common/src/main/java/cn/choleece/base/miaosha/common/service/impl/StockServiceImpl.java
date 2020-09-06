package cn.choleece.base.miaosha.common.service.impl;

import cn.choleece.base.miaosha.common.entity.Stock;
import cn.choleece.base.miaosha.common.mapper.StockMapper;
import cn.choleece.base.miaosha.common.service.IStockService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author choleece
 * @since 2020-04-25
 */
@Service
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock> implements IStockService {

    @Resource
    private StockMapper stockMapper;

    @Override
    public int getStockCount(int id) {
        Stock ssmStock = stockMapper.selectById(id);
        return ssmStock.getCount();
    }

    @Override
    public Stock getStockById(int id) {
        return stockMapper.selectById(id);
    }

    @Override
    public int updateStockById(Stock stock) {
        return stockMapper.updateById(stock);
    }

    @Override
    public int updateStockByOptimistic(Stock stock) {
        return stockMapper.updateByOptimistic(stock);
    }
}
