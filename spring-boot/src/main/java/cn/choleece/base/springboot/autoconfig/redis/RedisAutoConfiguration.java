package cn.choleece.base.springboot.autoconfig.redis;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-09-22 21:54
 **/
@Configuration
@EnableAutoConfiguration
public class RedisAutoConfiguration implements DisposableBean {
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void destroy() throws Exception {

    }
}
