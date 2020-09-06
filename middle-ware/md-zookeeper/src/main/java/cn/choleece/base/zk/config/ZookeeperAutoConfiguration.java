package cn.choleece.base.zk.config;

import cn.choleece.base.zk.client.ZkClientBean;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-16 22:22
 **/

//@Configuration
//@EnableConfigurationProperties(ZookeeperProperties.class)
public class ZookeeperAutoConfiguration {

//    @Bean
//    @ConditionalOnMissingBean({ZkClientBean.class})
    ZkClientBean zkClient(ZookeeperProperties properties) {
        System.out.println(properties.toString());
        ZkClientBean zkClient = new ZkClientBean(properties);
        return zkClient;
    }
}
