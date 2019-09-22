package cn.choleece.base.springboot.autoconfig.redis.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author choleece
 * @Description: Redis 配置
 * @Date 2019-09-22 00:59
 **/
@Data
@Component
@ConfigurationProperties(prefix = "spring.redis")
public class RedisConfig {

    private String host = "localhost";

    private int port = 6379;

}
