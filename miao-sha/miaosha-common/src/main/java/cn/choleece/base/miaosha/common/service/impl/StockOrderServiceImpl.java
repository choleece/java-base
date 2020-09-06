package cn.choleece.base.miaosha.common.service.impl;

import cn.choleece.base.md.kafka.producer.KafkaProducer;
import cn.choleece.base.miaosha.common.constant.RedisKeysConstant;
import cn.choleece.base.miaosha.common.entity.Stock;
import cn.choleece.base.miaosha.common.entity.StockOrder;
import cn.choleece.base.miaosha.common.mapper.StockOrderMapper;
import cn.choleece.base.miaosha.common.service.IStockOrderService;
import cn.choleece.base.miaosha.common.service.IStockService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author choleece
 * @since 2020-04-25
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class StockOrderServiceImpl extends ServiceImpl<StockOrderMapper, StockOrder> implements IStockOrderService {

    private static final Logger logger = LoggerFactory.getLogger(StockOrderServiceImpl.class);

    @Resource
    private IStockService stockService;
    @Resource
    private StockOrderMapper orderMapper;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private KafkaProducer kafkaProducer;

    @Value("${kafka.topic}")
    private String kafkaTopic;

    @Override
    public int createWrongOrder(int sid) throws Exception {

        //校验库存
        Stock stock = checkStock(sid);

        //扣库存
        saleStock(stock);

        //创建订单
        int id = createOrder(stock);

        return id;
    }

    @Override
    public int createOptimisticOrder(int sid) throws Exception {

        //校验库存
        Stock stock = checkStock(sid);

        //乐观锁更新库存
        saleStockOptimistic(stock);

        //创建订单
        int id = createOrder(stock);

        return id;
    }

    @Override
    public int createOptimisticOrderUseRedis(int sid) throws Exception {
        //检验库存，从 Redis 获取
        Stock stock = checkStockByRedis(sid);

        //乐观锁更新库存 以及更新 Redis
        saleStockOptimisticByRedis(stock);

        //创建订单
        int id = createOrder(stock);
        return id;
    }

    @Override
    public void createOptimisticOrderUseRedisAndKafka(int sid) throws Exception {

        //检验库存，从 Redis 获取
        Stock stock = checkStockByRedis(sid);

        // 利用 Kafka 创建订单
        kafkaProducer.send(kafkaTopic, stock.toString());
        logger.info("send Kafka success");
    }

    private Stock checkStockByRedis(int sid) throws Exception {

        redisTemplate.opsForValue().set("aaa", "bbb");

        System.out.println(redisTemplate.hasKey("stock:1:count"));
        System.out.println(redisTemplate.hasKey("stock:1:sale"));
        System.out.println(redisTemplate.hasKey("stock:1:version"));

        String countKey = String.format(RedisKeysConstant.STOCK_COUNT, sid);
        System.out.println(countKey);

        String saleKey = String.format(RedisKeysConstant.STOCK_COUNT, sid);
        System.out.println(saleKey);

        Object obj = redisTemplate.opsForValue().get("stock:1:count");
        System.out.println(obj);

        Integer count = (Integer) redisTemplate.opsForValue().get(countKey);
        Integer sale = (Integer) redisTemplate.opsForValue().get(saleKey);
        if (count <= 0) {
            throw new RuntimeException("库存不足 Redis currentCount=" + sale);
        }
        Integer version = (Integer) redisTemplate.opsForValue().get(String.format(RedisKeysConstant.STOCK_VERSION, sid));
        Stock stock = new Stock();
        stock.setId(String.valueOf(sid));
        stock.setCount(count);
        stock.setSale(sale);
        stock.setVersion(version);

        return stock;
    }

    /**
     * 乐观锁更新数据库 还要更新 Redis
     *
     * @param stock
     */
    private void saleStockOptimisticByRedis(Stock stock) {
        int count = stockService.updateStockByOptimistic(stock);
        if (count == 0) {
            throw new RuntimeException("并发更新库存失败");
        }
        // 自增
        redisTemplate.opsForValue().increment(String.format(RedisKeysConstant.STOCK_SALE, stock.getId()), 1);
        redisTemplate.opsForValue().increment(String.format(RedisKeysConstant.STOCK_VERSION, stock.getId()), 1);
    }

    private Stock checkStock(int sid) {
        Stock stock = stockService.getStockById(sid);
        if (stock.getCount() <= 0) {
            throw new RuntimeException("库存不足");
        }
        return stock;
    }

    private void saleStockOptimistic(Stock stock) {
        int count = stockService.updateStockByOptimistic(stock);
        if (count == 0) {
            throw new RuntimeException("并发更新库存失败");
        }
    }


    private int createOrder(Stock stock) {
        StockOrder order = new StockOrder();
        order.setSId(stock.getId());
        order.setName(stock.getName());
        int id = orderMapper.insert(order);
        return id;
    }

    private int saleStock(Stock stock) {
        stock.setSale(stock.getSale() + 1);
        stock.setCount(stock.getCount() - 1);
        return stockService.updateStockById(stock);
    }
}
