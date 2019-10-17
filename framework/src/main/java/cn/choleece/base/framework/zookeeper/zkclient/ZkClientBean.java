package cn.choleece.base.framework.zookeeper.zkclient;

import cn.choleece.base.framework.zookeeper.ZookeeperProperties;
import lombok.Data;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * @description: ZkClient 配置
 * @author: sf
 * @time: 2019-10-17 11:39
 *
 * 关于InitializingBean、DisposableBean的介绍，
 * 参考:https://www.cnblogs.com/xiaozhuanfeng/p/10415794.html
 */
@Data
public class ZkClientBean implements FactoryBean<ZkClient>, InitializingBean, DisposableBean {

    private static Logger logger = LogManager.getLogger(ZkClientBean.class);

    @NonNull
    private ZookeeperProperties properties;

    @NonNull
    private ZkClient zkClient;

    public ZkClientBean(ZookeeperProperties properties) {
        this.properties = properties;
    }

    @Override
    public ZkClient getObject() throws Exception {
        return this.zkClient;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    /**
     * 实现InitializingBean，在初始化完成后进行的操作
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(properties, "zookeeper properties can not be null...");

        this.zkClient = new ZkClient(properties.getUri(), properties.getSessionTimeout(), properties.getConnectionTimeout());
        this.zkClient.setZkSerializer(new CusZkSerializer());

        /**
         * s 为路径 o 为变化后的值 这类监听可以为作为配置中心等
         */
        this.zkClient.subscribeDataChanges("/dubbo/test", new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) throws Exception {
                System.out.println("数据有改变，哈哈哈哈哈哈" + "s: " + s + " o: " + o);
            }

            @Override
            public void handleDataDeleted(String s) throws Exception {
                System.out.println("数据有删除，哈哈哈哈哈哈哈");
            }
        });
        logger.info("zookeeper initialized...");
    }

    @Override
    public void destroy() throws Exception {
        logger.info("zk client bean start destroy...");
        this.zkClient.close();
        logger.info("zk client bean end destroy...");
    }
}
