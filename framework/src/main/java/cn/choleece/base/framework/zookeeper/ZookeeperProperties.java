package cn.choleece.base.framework.zookeeper;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author choleece
 * @Description: Zookeeper 属性配置
 * @Date 2019-10-16 22:21
 **/
@Data
@ConfigurationProperties(prefix = "choleece.zk")
public class ZookeeperProperties {

    private String uri = "127.0.0.1:2181";

    /**
     * session 超时时间
     */
    private int sessionTimeout = 1000;

    /**
     * 连接超时时间
     */
    private int connectionTimeout = 2147483647;
}
