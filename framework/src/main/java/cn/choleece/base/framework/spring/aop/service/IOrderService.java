package cn.choleece.base.framework.spring.aop.service;

import cn.choleece.base.framework.spring.aop.entity.Order;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-11-23 22:35
 **/
public interface IOrderService {

    Order createOrder(String username, String product);

    Order queryOrder(String username);
}
