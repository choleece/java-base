package cn.choleece.base.framework.spring.aop.service.impl;

import cn.choleece.base.framework.spring.aop.entity.Order;
import cn.choleece.base.framework.spring.aop.service.IOrderService;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-11-23 22:38
 **/
public class OrderServiceImpl implements IOrderService {

    @Override
    public Order createOrder(String username, String product) {
        Order order = new Order();
        order.setUsername(username);
        order.setProduct(product);
        return order;
    }

    @Override
    public Order queryOrder(String username) {
        Order order = new Order();
        order.setUsername("test");
        order.setProduct("test");
        return order;
    }
}
