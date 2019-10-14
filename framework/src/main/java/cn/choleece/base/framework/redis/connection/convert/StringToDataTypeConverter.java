package cn.choleece.base.framework.redis.connection.convert;

import cn.choleece.base.framework.redis.connection.DataType;
import org.springframework.core.convert.converter.Converter;

/**
 * @description: 字符串与Redis数据类型转换
 * @author: sf
 * @time: 2019-10-14 18:18
 */
public class StringToDataTypeConverter implements Converter<String, DataType> {
    public StringToDataTypeConverter() {
    }

    public DataType convert(String source) {
        return DataType.fromCode(source);
    }
}
