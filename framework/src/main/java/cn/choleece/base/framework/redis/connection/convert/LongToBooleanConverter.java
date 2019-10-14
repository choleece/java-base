package cn.choleece.base.framework.redis.connection.convert;

import org.springframework.core.convert.converter.Converter;

/**
 * @description: 整型转换
 * @author: sf
 * @time: 2019-10-14 18:18
 */
public class LongToBooleanConverter implements Converter<Long, Boolean> {

    public LongToBooleanConverter() {
    }

    public Boolean convert(Long result) {
        return result == 1L;
    }
}
