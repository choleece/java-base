package cn.choleece.base.framework.redis.connection.jedis;

import cn.choleece.base.framework.exception.DataAccessException;
import cn.choleece.base.framework.exception.InvalidDataAccessApiUsageException;
import cn.choleece.base.framework.redis.ClusterRedirectException;
import cn.choleece.base.framework.redis.RedisConnectionFailureException;
import cn.choleece.base.framework.redis.TooManyClusterRedirectionsException;
import org.springframework.core.convert.converter.Converter;
import redis.clients.jedis.exceptions.*;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-14 22:40
 **/
public class JedisExceptionConverter implements Converter<Exception, DataAccessException> {

    public JedisExceptionConverter() {
    }

    @Override
    public DataAccessException convert(Exception ex) {
        if (ex instanceof DataAccessException) {
            return (DataAccessException)ex;
        } else if (ex instanceof JedisDataException) {
            if (ex instanceof JedisRedirectionException) {
                JedisRedirectionException re = (JedisRedirectionException)ex;
                return new ClusterRedirectException(re.getSlot(), re.getTargetNode().getHost(), re.getTargetNode().getPort(), ex);
            } else {
                return (DataAccessException)(ex instanceof JedisClusterMaxRedirectionsException ? new TooManyClusterRedirectionsException(ex.getMessage(), ex) : new InvalidDataAccessApiUsageException(ex.getMessage(), ex));
            }
        } else if (ex instanceof JedisConnectionException) {
            return new RedisConnectionFailureException(ex.getMessage(), ex);
        } else if (ex instanceof JedisException) {
            return new InvalidDataAccessApiUsageException(ex.getMessage(), ex);
        } else if (ex instanceof UnknownHostException) {
            return new RedisConnectionFailureException("Unknown host " + ex.getMessage(), ex);
        } else {
            return ex instanceof IOException ? new RedisConnectionFailureException("Could not connect to Redis server", ex) : null;
        }
    }
}
