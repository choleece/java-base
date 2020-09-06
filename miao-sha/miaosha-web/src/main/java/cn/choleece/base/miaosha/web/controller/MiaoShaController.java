package cn.choleece.base.miaosha.web.controller;

import cn.choleece.base.md.redis.distribute.annotation.SpringControllerLimit;
import cn.choleece.base.miaosha.common.base.BaseController;
import cn.choleece.base.miaosha.common.service.IStockOrderService;
import cn.choleece.base.miaosha.common.service.IStockService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-04-27 23:23
 **/
@RestController
@RequestMapping("/miaosha")
@Slf4j
public class MiaoShaController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(MiaoShaController.class);

    @Resource
    private IStockService stockService;
    @Resource
    private IStockOrderService orderService;

    @RequestMapping("/health")
    public String health() {
        logger.info("heath");
        return "OK";
    }

    @RequestMapping("/getStockNum")
    public String getStockNum(@RequestParam Integer sid) {
        int currentCount = 0;
        try {
            currentCount = stockService.getStockCount(sid);
        } catch (Exception e) {
            logger.error("Exception",e);
        }
        logger.info("currentCount={}", currentCount);
        return String.valueOf(currentCount);
    }


    @RequestMapping("/createWrongOrder/{sid}")
    public String createWrongOrder(@PathVariable int sid) {
        logger.info("sid=[{}]", sid);
        int id = 0;
        try {
            id = orderService.createWrongOrder(sid);
        } catch (Exception e) {
            logger.error("Exception",e);
        }
        return String.valueOf(id);
    }


    /**
     * 乐观锁更新库存
     * @param sid
     * @return
     */
    @RequestMapping("/createOptimisticOrder/{sid}")
    public String createOptimisticOrder(@PathVariable int sid) {
        logger.info("sid=[{}]", sid);
        int id = 0;
        try {
            id = orderService.createOptimisticOrder(sid);
        } catch (Exception e) {
            logger.error("Exception",e);
        }
        return String.valueOf(id);
    }

    /**
     * 乐观锁更新库存 限流
     * @param sid
     * @return
     */
    @SpringControllerLimit(errorCode = 200)
    @RequestMapping("/createOptimisticLimitOrder/{sid}")
    @ResponseBody
    public String createOptimisticLimitOrder(@PathVariable int sid) {
        logger.info("sid=[{}]", sid);
        int id = 0;
        try {
            id = orderService.createOptimisticOrder(sid);
        } catch (Exception e) {
            logger.error("Exception",e);
        }
        return String.valueOf(id);
    }

    /**
     * 乐观锁更新库存 限流 库存改为查询 Redis 提高性能
     * @param sid
     * @return
     */
    @SpringControllerLimit(errorCode = 200,errorMsg = "request has limited")
    @RequestMapping("/createOptimisticLimitOrderByRedis/{sid}")
    @ResponseBody
    public String createOptimisticLimitOrderByRedis(@PathVariable int sid) {
        logger.info("sid=[{}]", sid);
        int id = 0;
        try {
            id = orderService.createOptimisticOrderUseRedis(sid);
        } catch (Exception e) {
            logger.error("Exception",e);
        }
        return String.valueOf(id);
    }

    /**
     * 乐观锁更新库存 限流 库存改为查询 Redis 提高性能
     * 异步创建订单 Kafka
     * @param sid
     * @return
     */
    @SpringControllerLimit
    @RequestMapping("/createOptimisticLimitOrderByRedisAndKafka/{sid}")
    @ResponseBody
    public String createOptimisticLimitOrderByRedisAndKafka(@PathVariable int sid) {
        logger.info("sid=[{}]", sid);
        int id = 0;
        try {
            orderService.createOptimisticOrderUseRedisAndKafka(sid);
        } catch (Exception e) {
            logger.error("Exception",e);
        }
        return String.valueOf(id);
    }
}
