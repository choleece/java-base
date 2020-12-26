package cn.choleece.base.springsource.listener;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-12-18 21:24
 **/
public class ApplicationEnvironmentPreparedEventListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        System.out.println("ApplicationEnvironmentPreparedEvent---------------");
    }
}
