package cn.choleece.base.springboot.event;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author choleece
 * @Description: 路由状态包装类
 * @Date 2020-09-05 21:00
 **/
@Data
@Accessors(chain = true)
public class RouteStatus {

    /**
     * 订单号
     */
    private String orderId;
    /**
     * 运单号
     */
    private String mailNo;
    /**
     * 路由状态
     */
    private String orderStatus;
}
