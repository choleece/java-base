package cn.choleece.base.miaosha.web.controller;

import cn.choleece.base.miaosha.common.base.BaseController;
import cn.choleece.base.miaosha.common.controller.param.CreateOrderParam;
import cn.choleece.base.miaosha.common.service.IMiaoshaOrderService;
import cn.choleece.base.miaosha.common.util.R;
import cn.choleece.base.miaosha.common.util.ratelimit.RateLimit;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-04-27 23:23
 **/
@RestController
@RequestMapping("/miaosha")
@Slf4j
public class MiaoShaController extends BaseController {
    @Resource
    private RateLimit rateLimit;
    @Autowired
    private IMiaoshaOrderService miaoshaOrderService;


    @GetMapping("")
    public R miaosha() {
        System.out.println(Thread.currentThread().getName());
        if (rateLimit.acquire("limit", 10)) {
            // todo 缓存内减少订单
            return R.ok();
        }
        return R.error("你被限流了....");
    }

    /**
     * 此方法会导致超卖
     * @param createOrderModel
     * @return
     */
    @PostMapping("/order/nothing")
    public R orderWithNothing(@RequestBody @Valid CreateOrderParam createOrderModel) {
        log.info("create miaosha order with nothing... params {}", createOrderModel.toString());
        return miaoshaOrderService.createOrder(createOrderModel);
    }

    @PostMapping("/order/db/lock/pessimistic")
    public R orderWithDbLock(@RequestBody @Valid CreateOrderParam createOrderModel) {
        log.info("create miaosha order with db lock... params {}", createOrderModel.toString());

        return miaoshaOrderService.createOrderWithPessimisticDbLock(createOrderModel);
    }

    @PostMapping("/order/db/lock/lucky")
    public R orderWithDbLuckyLock(@RequestBody @Valid CreateOrderParam createOrderModel) throws Exception {
        log.info("create miaosha order with db lucky lock... params {}", createOrderModel.toString());

        return miaoshaOrderService.createOrderWithPessimisticDbLuckyLock(createOrderModel);
    }

    @PostMapping("/order/redis")
    public R orderWithRedis(@RequestBody @Valid CreateOrderParam createOrderModel) throws Exception {
        log.info("create miaosha order with redis... params {}", createOrderModel.toString());

        return miaoshaOrderService.createOrderWithRedis(createOrderModel);
    }

    public static void main(String[] args) {
        CreateOrderParam orderModel = new CreateOrderParam();
        orderModel.setGoodsId(3L);
        orderModel.setDeliveryAddrId(1L);
        orderModel.setGoodsCount(2);
        orderModel.setGoodsId(3L);
        orderModel.setUserId(1L);

        System.out.println(JSONObject.toJSONString(orderModel));
    }
}
