package cn.choleece.base.framework.zookeeper;

import org.I0Itec.zkclient.ZkClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-16 22:22
 **/

@Configuration
@EnableConfigurationProperties(ZookeeperProperties.class)
public class ZookeeperAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean({ZkClient.class})
    ZkClient zkClient() {
        ZkClient zkClient = new ZkClient("");
        return zkClient;
    }
}
