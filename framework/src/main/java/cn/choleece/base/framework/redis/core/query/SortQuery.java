package cn.choleece.base.framework.redis.core.query;

import cn.choleece.base.framework.redis.connection.SortParameters;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-15 22:57
 **/
public interface SortQuery<K> {
    K getKey();

    @Nullable
    SortParameters.Order getOrder();

    @Nullable
    Boolean isAlphabetic();

    @Nullable
    SortParameters.Range getLimit();

    @Nullable
    String getBy();

    List<String> getGetPattern();
}
