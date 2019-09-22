package cn.choleece.base.springboot.autoconfig.redis;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-09-22 21:54
 **/
@Configuration
@EnableAutoConfiguration
public class RedisAutoConfiguration implements DisposableBean {
    public void destroy() throws Exception {

    }
}
