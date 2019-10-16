package cn.choleece.base.framework.redis.core;

import cn.choleece.base.framework.redis.connection.RedisConnectionFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * @author choleece
 * @Description: Redis 存取器
 * @Date 2019-10-15 21:25
 **/
public class RedisAccessor implements InitializingBean {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Nullable
    private RedisConnectionFactory connectionFactory;

    public RedisAccessor() {
    }

    @Override
    public void afterPropertiesSet() {
        Assert.state(this.getConnectionFactory() != null, "RedisConnectionFactory is required");
    }

    @Nullable
    public RedisConnectionFactory getConnectionFactory() {
        return this.connectionFactory;
    }

    public RedisConnectionFactory getRequiredConnectionFactory() {
        RedisConnectionFactory connectionFactory = this.getConnectionFactory();
        if (connectionFactory == null) {
            throw new IllegalStateException("RedisConnectionFactory is required");
        } else {
            return connectionFactory;
        }
    }

    public void setConnectionFactory(RedisConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }
}
