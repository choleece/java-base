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

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.PassThroughExceptionTranslationStrategy#translate(java.lang.Exception)
     */
    @Override
    public DataAccessException translate(Exception e) {

        DataAccessException translated = super.translate(e);
        return translated != null ? translated : getFallback(e);
    }

    /**
     * Returns a new {@link RedisSystemException} wrapping the given {@link Exception}.
     *
     * @param e causing exception.
     * @return the fallback exception.
     */
    protected RedisSystemException getFallback(Exception e) {
        return new RedisSystemException("Unknown redis exception", e);
    }
}
