package cn.choleece.base.framework.redis.connection.convert;

import cn.choleece.base.framework.redis.RedisSystemException;
import org.springframework.core.convert.converter.Converter;

import java.io.StringReader;
import java.util.Properties;

/**
 * @description: 字符串转换
 * @author: sf
 * @time: 2019-10-14 18:16
 */
public class StringToPropertiesConverter implements Converter<String, Properties> {

    public StringToPropertiesConverter() {
    }

    public Properties convert(String source) {
        Properties info = new Properties();

        try {
            StringReader stringReader = new StringReader(source);
            Throwable var4 = null;

            try {
                info.load(stringReader);
            } catch (Throwable var14) {
                var4 = var14;
                throw var14;
            } finally {
                if (stringReader != null) {
                    if (var4 != null) {
                        try {
                            stringReader.close();
                        } catch (Throwable var13) {
                            var4.addSuppressed(var13);
                        }
                    } else {
                        stringReader.close();
                    }
                }

            }

            return info;
        } catch (Exception var16) {
            throw new RedisSystemException("Cannot read Redis info", var16);
        }
    }
}
