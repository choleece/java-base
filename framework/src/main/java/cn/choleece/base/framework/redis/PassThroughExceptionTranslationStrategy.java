package cn.choleece.base.framework.redis;

import cn.choleece.base.framework.exception.DataAccessException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

/**
 * @description: TODO
 * @author: sf
 * @time: 2019-10-14 17:35
 */
public class PassThroughExceptionTranslationStrategy implements ExceptionTranslationStrategy {

    private final Converter<Exception, DataAccessException> converter;

    public PassThroughExceptionTranslationStrategy(Converter<Exception, DataAccessException> converter) {
        this.converter = converter;
    }

    @Nullable
    public DataAccessException translate(Exception e) {
        return (DataAccessException)this.converter.convert(e);
    }

}
