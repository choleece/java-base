package cn.choleece.base.zk.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-07-11 15:44
 **/
@Data
@ConfigurationProperties(prefix = "choleece.zookeeper.curator")
public class ZkCuratorProperty {

    /**
     * zk 地址ip:port
     */
    private String server;

    /**
     * session超时时间
     */
    private Integer sessionTimeoutMs;

    /**
     * 连接超时时间
     */
    private Integer connectionTimeoutMs;

    /**
     * 最大重试次数
     */
    private Integer maxRetries;

    /**
     * 重试间隔时间
     */
    private Integer sleepMsBetweenRetries;

}
