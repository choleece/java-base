package cn.choleece.base.framework.redis.connection.convert;

import org.springframework.core.convert.converter.Converter;

import java.util.Map;
import java.util.Properties;

/**
 * @description: TODO
 * @author: sf
 * @time: 2019-10-14 18:23
 */
public enum MapToPropertiesConverter implements Converter<Map<?, ?>, Properties> {
    INSTANCE;

    private MapToPropertiesConverter() {
    }

    public Properties convert(Map<?, ?> source) {
        Properties target = new Properties();
        target.putAll(source);
        return target;
    }
}