package cn.choleece.base.springboot.controller;

import cn.choleece.base.springboot.event.RouteStatus;
import cn.choleece.base.springboot.service.RouteStatusService;
import cn.choleece.base.springboot.utils.LogUtils;
import org.slf4j.Logger;
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

    private static final Logger log = LogUtils.clazzLog(RouteStatusController.class);

    @GetMapping("/accept")
    public String acceptRouteStatus() {

        routeStatusService.acceptRouteStatus(new RouteStatus().setOrderId("1").setMailNo("2").setOrderStatus("3"));

        return "OK";
    }

    @GetMapping("/log")
    public String log() {
        LogUtils.consoleLog.info("console log info");
        LogUtils.consoleLog.warn("console log warn");
        LogUtils.consoleLog.error("console log error");

        LogUtils.infoLog.info("info log info");
        LogUtils.infoLog.warn("info log warn");
        LogUtils.infoLog.error("info log error");

        LogUtils.warnLog.info("warn log info");
        LogUtils.warnLog.warn("warn log warn");
        LogUtils.warnLog.error("warn log error");

        LogUtils.errorLog.info("error log info");
        LogUtils.errorLog.warn("error log warn");
        LogUtils.errorLog.error("error log error");

        log.info("class log info");
        log.warn("class log warn");
        log.error("class log error");

        return "OK";
    }
}
