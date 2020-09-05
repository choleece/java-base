package cn.choleece.base.springboot.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author choleece
 * @Description: 路由状态到达事件
 * @Date 2020-09-05 20:57
 **/
@Getter
public class RouteStatusEvent extends ApplicationEvent {

    private RouteStatus routeStatus;

    public RouteStatusEvent(Object source, RouteStatus routeStatus) {
        super(source);
        this.routeStatus = routeStatus;
    }
}
