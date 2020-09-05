package cn.choleece.base.springboot.listener;

import cn.choleece.base.springboot.event.RouteStatusEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author choleece
 * @Description: 将路由信息转发到MQ
 * @Date 2020-09-05 21:08
 **/
@Component
public class PushRouteStatusToMqListener {

    @EventListener
    public void pushRouteStatusToMq(RouteStatusEvent statusEvent) {
        System.out.println("push data to mq " + statusEvent.getRouteStatus());
    }
}
