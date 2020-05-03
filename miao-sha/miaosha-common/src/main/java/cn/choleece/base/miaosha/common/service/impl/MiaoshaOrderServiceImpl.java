package cn.choleece.base.miaosha.common.service.impl;

import cn.choleece.base.miaosha.common.controller.model.CreateOrderModel;
import cn.choleece.base.miaosha.common.entity.Goods;
import cn.choleece.base.miaosha.common.entity.MiaoshaGoods;
import cn.choleece.base.miaosha.common.entity.MiaoshaOrder;
import cn.choleece.base.miaosha.common.entity.OrderInfo;
import cn.choleece.base.miaosha.common.mapper.GoodsMapper;
import cn.choleece.base.miaosha.common.mapper.MiaoshaGoodsMapper;
import cn.choleece.base.miaosha.common.mapper.MiaoshaOrderMapper;
import cn.choleece.base.miaosha.common.mapper.OrderInfoMapper;
import cn.choleece.base.miaosha.common.service.IMiaoshaOrderService;
import cn.choleece.base.miaosha.common.util.R;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author choleece
 * @since 2020-04-25
 */
@Service
public class MiaoshaOrderServiceImpl extends ServiceImpl<MiaoshaOrderMapper, MiaoshaOrder> implements IMiaoshaOrderService {
    @Autowired
    private MiaoshaGoodsMapper miaoshaGoodsMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private MiaoshaOrderMapper miaoshaOrderMapper;

    @Override
    @Transactional
    public R createOrder(CreateOrderModel createOrderModel) {
        Goods goods = goodsMapper.selectById(createOrderModel.getGoodsId());
        if (goods == null) {
            return R.error("您购买的商品不存在");
        }

        MiaoshaGoods dbMiaoshaGoods = miaoshaGoodsMapper.selectById(createOrderModel.getGoodsId());
        if (dbMiaoshaGoods == null) {
            return R.error("您购买的商品不存在");
        }

        if (dbMiaoshaGoods.getStockCount() - createOrderModel.getGoodsCount() < 0) {
            return R.error(String.format("购买的商品个数超出库存，现有库存为%s", dbMiaoshaGoods.getStockCount()));
        }

        MiaoshaGoods updateMiaoshaGoods = new MiaoshaGoods();
        updateMiaoshaGoods.setId(dbMiaoshaGoods.getId());
        updateMiaoshaGoods.setStockCount(dbMiaoshaGoods.getStockCount() - createOrderModel.getGoodsCount());

        // 更新秒杀商品库存
        miaoshaGoodsMapper.updateById(updateMiaoshaGoods);

        Goods updateGoods = new Goods();
        updateGoods.setId(createOrderModel.getGoodsId());
        updateGoods.setGoodsStock(goods.getGoodsStock() - createOrderModel.getGoodsCount());

        // 更新商品库存
        goodsMapper.updateById(updateGoods);

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setUserId(createOrderModel.getUserId());
        orderInfo.setGoodsId(createOrderModel.getGoodsId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setDeliveryAddrId(1L);
        orderInfo.setGoodsCount(createOrderModel.getGoodsCount());
        orderInfo.setGoodsPrice(goods.getGoodsPrice());

        // 插入订单
        orderInfoMapper.insert(orderInfo);

        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setGoodsId(createOrderModel.getGoodsId());
        miaoshaOrder.setUserId(createOrderModel.getUserId());
        miaoshaOrder.setOrderId(Long.valueOf(orderInfo.getId()));

        // 插入秒杀关联订单
        miaoshaOrderMapper.insert(miaoshaOrder);

        return R.ok(orderInfo.getId());
    }

    @Override
    public R createOrderWithPessimisticDbLock(CreateOrderModel createOrderModel) {
        Goods goods = goodsMapper.selectById(createOrderModel.getGoodsId());
        if (goods == null) {
            return R.error("您购买的商品不存在");
        }

        MiaoshaGoods dbMiaoshaGoods = miaoshaGoodsMapper.selectOne(new QueryWrapper<MiaoshaGoods>().eq("goods_id", createOrderModel.getGoodsId()).last(" for update"));
        if (dbMiaoshaGoods == null) {
            return R.error("您购买的商品不存在");
        }

        if (dbMiaoshaGoods.getStockCount() - createOrderModel.getGoodsCount() < 0) {
            return R.error(String.format("购买的商品个数超出库存，现有库存为%s", dbMiaoshaGoods.getStockCount()));
        }

        MiaoshaGoods updateMiaoshaGoods = new MiaoshaGoods();
        updateMiaoshaGoods.setId(dbMiaoshaGoods.getId());
        updateMiaoshaGoods.setStockCount(dbMiaoshaGoods.getStockCount() - createOrderModel.getGoodsCount());

        // 更新秒杀商品库存
        miaoshaGoodsMapper.updateById(updateMiaoshaGoods);

        Goods updateGoods = new Goods();
        updateGoods.setId(createOrderModel.getGoodsId());
        updateGoods.setGoodsStock(goods.getGoodsStock() - createOrderModel.getGoodsCount());

        // 更新商品库存
        goodsMapper.updateById(updateGoods);

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setUserId(createOrderModel.getUserId());
        orderInfo.setGoodsId(createOrderModel.getGoodsId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setDeliveryAddrId(1L);
        orderInfo.setGoodsCount(createOrderModel.getGoodsCount());
        orderInfo.setGoodsPrice(goods.getGoodsPrice());

        // 插入订单
        orderInfoMapper.insert(orderInfo);

        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setGoodsId(createOrderModel.getGoodsId());
        miaoshaOrder.setUserId(createOrderModel.getUserId());
        miaoshaOrder.setOrderId(Long.valueOf(orderInfo.getId()));

        // 插入秒杀关联订单
        miaoshaOrderMapper.insert(miaoshaOrder);

        return R.ok(orderInfo.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R createOrderWithPessimisticDbLuckyLock(CreateOrderModel createOrderModel) throws Exception {

        Goods goods = goodsMapper.selectById(createOrderModel.getGoodsId());
        if (goods == null) {
            return R.error("您购买的商品不存在");
        }

        MiaoshaGoods dbMiaoshaGoods = miaoshaGoodsMapper.selectOne(new QueryWrapper<MiaoshaGoods>().eq("goods_id", createOrderModel.getGoodsId()));
        if (dbMiaoshaGoods == null) {
            return R.error("您购买的商品不存在");
        }

        if (dbMiaoshaGoods.getStockCount() - createOrderModel.getGoodsCount() < 0) {
            return R.error(String.format("购买的商品个数超出库存，现有库存为%s", dbMiaoshaGoods.getStockCount()));
        }

        MiaoshaGoods updateMiaoshaGoods = new MiaoshaGoods();
        updateMiaoshaGoods.setId(dbMiaoshaGoods.getId());
        updateMiaoshaGoods.setStockCount(dbMiaoshaGoods.getStockCount() - createOrderModel.getGoodsCount());

        // 更新秒杀商品库存
        int records = miaoshaGoodsMapper.updateMiaoshaGoods(updateMiaoshaGoods);
        System.out.println("更新商品数量为: " + records);
        if (records == 0) {
            throw new Exception("您秒杀的商品已完成");
        }

        Goods updateGoods = new Goods();
        updateGoods.setId(createOrderModel.getGoodsId());
        updateGoods.setGoodsStock(goods.getGoodsStock() - createOrderModel.getGoodsCount());

        // 更新商品库存
        goodsMapper.updateById(updateGoods);

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setUserId(createOrderModel.getUserId());
        orderInfo.setGoodsId(createOrderModel.getGoodsId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setDeliveryAddrId(1L);
        orderInfo.setGoodsCount(createOrderModel.getGoodsCount());
        orderInfo.setGoodsPrice(goods.getGoodsPrice());

        // 插入订单
        orderInfoMapper.insert(orderInfo);

        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setGoodsId(createOrderModel.getGoodsId());
        miaoshaOrder.setUserId(createOrderModel.getUserId());
        miaoshaOrder.setOrderId(Long.valueOf(orderInfo.getId()));

        // 插入秒杀关联订单
        miaoshaOrderMapper.insert(miaoshaOrder);

        return R.ok(orderInfo.getId());
    }

    @Override
    public R createOrderWithRedis(CreateOrderModel createOrderModel) {
        // 系统启动时，将数量加载进缓存
        // 获取分布式锁
        // 从缓存内获取数量，针对数量进行判断
        // 针对数量进行预扣减
        // 生成订单，扣减库存，或者通过mq针对订单进行操作
        return null;
    }
}
