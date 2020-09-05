package cn.choleece.base.springboot.controller;

import cn.choleece.base.springboot.event.RouteStatus;
import cn.choleece.base.springboot.service.RouteStatusService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author choleece
 * @Description: route status accept controller
 * @Date 2020-09-05 21:12
 **/
@RestController
@RequestMapping("/route/status")
public class RouteStatusController {
    @Resource
    private RouteStatusService routeStatusService;

    @GetMapping("/accept")
    public String acceptRouteStatus() {

        routeStatusService.acceptRouteStatus(new RouteStatus().setOrderId("1").setMailNo("2").setOrderStatus("3"));

        return "OK";
    }
}
