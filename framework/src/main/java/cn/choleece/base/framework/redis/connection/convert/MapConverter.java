package cn.choleece.base.framework.redis.connection.convert;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-14 22:27
 **/
public class MapConverter <S, T> implements Converter<Map<S, S>, Map<T, T>> {
    private Converter<S, T> itemConverter;

    public MapConverter(Converter<S, T> itemConverter) {
        this.itemConverter = itemConverter;
    }

    @Override
    public Map<T, T> convert(Map<S, S> source) {
        return (Map)source.entrySet().stream().collect(Collectors.toMap((e) -> {
            return this.itemConverter.convert(e.getKey());
        }, (e) -> {
            return this.itemConverter.convert(e.getValue());
        }, (a, b) -> {
            return a;
        }, source instanceof LinkedHashMap ? LinkedHashMap::new : HashMap::new));
    }
}
