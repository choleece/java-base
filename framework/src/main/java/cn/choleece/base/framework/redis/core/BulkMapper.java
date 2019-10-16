package cn.choleece.base.framework.redis.core;

import java.util.List;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-15 22:58
 **/
public interface BulkMapper<T, V> {
    T mapBulk(List<V> var1);
}
