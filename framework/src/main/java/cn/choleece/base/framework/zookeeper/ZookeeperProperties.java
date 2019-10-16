package cn.choleece.base.framework.zookeeper;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-16 22:21
 **/
@ConfigurationProperties(prefix = "choleece.zk")
public class ZookeeperProperties {

    private String uri = "127.0.0.1:2181";
}
