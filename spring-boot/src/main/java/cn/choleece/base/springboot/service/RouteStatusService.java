package cn.choleece.base.springboot.service;

import cn.choleece.base.springboot.event.RouteStatus;
import cn.choleece.base.springboot.event.RouteStatusEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author choleece
 * @Description: service of route status
 * @Date 2020-09-05 21:13
 **/
@Service
public class RouteStatusService {
    @Resource
    private ApplicationContext applicationContext;

    public void acceptRouteStatus(RouteStatus status) {
        System.out.println("accept route status " + status.toString());

        applicationContext.publishEvent(new RouteStatusEvent(this, status));
    }

}
