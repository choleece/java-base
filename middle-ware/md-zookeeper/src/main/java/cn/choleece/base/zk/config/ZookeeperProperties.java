package cn.choleece.base.zk.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author choleece
 * @Description: Zookeeper 属性配置
 * @Date 2019-10-16 22:21
 **/
@ConfigurationProperties(prefix = "choleece.zk")
public class ZookeeperProperties {

    private String uri = "127.0.0.1:2181";

    /**
     * session 超时时间
     */
    private int sessionTimeout = 1000;

    /**
     * 连接超时时间(服务连接不可达时间，类似HTTP连接超时)
     */
    private int connectionTimeout = 2147483647;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    @Override
    public String toString() {
        return "ZookeeperProperties{" +
                "uri='" + uri + '\'' +
                ", sessionTimeout=" + sessionTimeout +
                ", connectionTimeout=" + connectionTimeout +
                '}';
    }
}
