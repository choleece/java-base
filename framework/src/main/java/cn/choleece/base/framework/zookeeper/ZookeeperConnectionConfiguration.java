package cn.choleece.base.framework.zookeeper;

import org.I0Itec.zkclient.ZkClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-16 22:26
 **/

@Configuration
@ConditionalOnClass(ZkClient.class)
public class ZookeeperConnectionConfiguration {

}
