package cn.choleece.base.framework.redis.connection;

import org.springframework.lang.Nullable;

import java.util.List;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-15 22:21
 **/
public enum ReturnType {

    BOOLEAN,
    INTEGER,
    MULTI,
    STATUS,
    VALUE;

    private ReturnType() {
    }

    public static ReturnType fromJavaType(@Nullable Class<?> javaType) {
        if (javaType == null) {
            return STATUS;
        } else if (javaType.isAssignableFrom(List.class)) {
            return MULTI;
        } else if (javaType.isAssignableFrom(Boolean.class)) {
            return BOOLEAN;
        } else {
            return javaType.isAssignableFrom(Long.class) ? INTEGER : VALUE;
        }
    }
}
