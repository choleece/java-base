package cn.choleece.base.framework.redis.connection;

import org.springframework.lang.Nullable;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-13 22:27
 **/
public interface NamedNode {

    @Nullable
    String getName();
}
