package cn.choleece.base.framework.redis;

import cn.choleece.base.framework.exception.DataAccessException;
import org.springframework.core.convert.converter.Converter;

/**
 * @description: TODO
 * @author: sf
 * @time: 2019-10-14 17:35
 */
public class FallbackExceptionTranslationStrategy extends PassThroughExceptionTranslationStrategy {

    public FallbackExceptionTranslationStrategy(Converter<Exception, DataAccessException> converter) {
        super(converter);
    }

    @Override
    public DataAccessException translate(Exception e) {
        DataAccessException translated = super.translate(e);
        return (DataAccessException)(translated != null ? translated : this.getFallback(e));
    }

    protected RedisSystemException getFallback(Exception e) {
        return new RedisSystemException("Unknown redis exception", e);
    }

}
