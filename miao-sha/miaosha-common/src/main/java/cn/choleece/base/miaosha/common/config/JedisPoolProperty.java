package cn.choleece.base.miaosha.common.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author choleece
 * @Description: jedis 相关属性
 * @Date 2020-04-27 07:33
 **/
@Component
@ConfigurationProperties(prefix = "redis.jedis")
@ConditionalOnProperty(prefix = "redis.jedis", value = "host")
@Data
public class JedisPoolProperty {

    private String host;

    private int port;

    private int timeout;

    private int maxTotal;

    private int maxIdle;

    private int maxWaitMills;

    private Boolean testOnBorrow;

    private Boolean testOnReturn;

}
