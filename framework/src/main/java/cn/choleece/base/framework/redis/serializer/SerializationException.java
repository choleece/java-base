package cn.choleece.base.framework.redis.serializer;

import org.springframework.core.NestedRuntimeException;

/**
 * @author choleece
 * @Description: 序列化异常
 * @Date 2019-10-15 21:36
 **/
public class SerializationException extends NestedRuntimeException {
    public SerializationException(String msg) {
        super(msg);
    }

    public SerializationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
