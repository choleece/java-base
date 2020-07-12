package cn.choleece.base.zk.config;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author choleece
 * @Description: ConditionalOnProperty 可以指定配置文件properties包含什么，如果包含，则报错
 * @Date 2020-07-11 15:49
 **/
@Configuration
@EnableConfigurationProperties({ZkCuratorProperty.class})
@ConditionalOnProperty(prefix = "choleece.zookeeper.curator", name = "server")
public class ZkConfiguration {
    @Autowired
    private ZkCuratorProperty property;

    @Bean(initMethod = "start")
    public CuratorFramework curatorFramework() {
        return CuratorFrameworkFactory.newClient(
                property.getServer(),
                property.getSessionTimeoutMs(),
                property.getConnectionTimeoutMs(),
                new RetryNTimes(property.getMaxRetries(), property.getSleepMsBetweenRetries()));
    }
}
