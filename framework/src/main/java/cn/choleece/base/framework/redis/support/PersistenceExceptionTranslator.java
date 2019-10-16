package cn.choleece.base.framework.redis.support;

import cn.choleece.base.framework.exception.DataAccessException;
import org.springframework.lang.Nullable;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-15 21:22
 **/
public interface PersistenceExceptionTranslator {

    @Nullable
    DataAccessException translateExceptionIfPossible(RuntimeException var1);
}
