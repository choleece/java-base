package cn.choleece.base.springboot.listener;

import cn.choleece.base.springboot.event.RouteStatus;
import cn.choleece.base.springboot.event.RouteStatusEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author choleece
 * @Description: 转发路由监听器
 * @Date 2020-09-05 21:04
 **/
@Component
public class ForwardRouteStatusListener {

    @EventListener
    public void forwardRouteStatus(RouteStatusEvent statusEvent) {
        System.out.println("froward route status to third party " + statusEvent.getRouteStatus());
        postRouteStatusToThirdParty(statusEvent.getRouteStatus());
    }

    private void postRouteStatusToThirdParty(final RouteStatus routeStatus) {
        List<String> thirdPartyUrls = getThirdPartyRouteStatusAcceptUrls();

        if (thirdPartyUrls != null && !thirdPartyUrls.isEmpty()) {
            thirdPartyUrls.forEach(url -> pushRouteStatus(url, routeStatus));
        }
    }

    private List<String> getThirdPartyRouteStatusAcceptUrls() {
        return Collections.emptyList();
    }

    private void pushRouteStatus(String url, RouteStatus status) {
        try {
            // todo req third party through http
            // if req is suc, finish the method
            // if req is fail, need to handle the req, maybe retry or persist to the storage
        } catch (Exception e) {
            handleReqError(status);
        }
    }

    private void handleReqError(final RouteStatus status) {
        // todo handle req error
    }

}
