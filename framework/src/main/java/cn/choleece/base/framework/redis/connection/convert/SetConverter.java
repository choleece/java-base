package cn.choleece.base.framework.redis.connection.convert;

import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-14 22:29
 **/
public class SetConverter<S, T> implements Converter<Set<S>, Set<T>> {
    private Converter<S, T> itemConverter;

    public SetConverter(Converter<S, T> itemConverter) {
        Assert.notNull(itemConverter, "ItemConverter must not be null!");
        this.itemConverter = itemConverter;
    }

    @Override
    public Set<T> convert(Set<S> source) {
        Stream var10000 = source.stream();
        Converter var10001 = this.itemConverter;
        var10001.getClass();
        return (Set)var10000.map(var10001::convert).collect(Collectors.toCollection(source instanceof LinkedHashSet ? LinkedHashSet::new : HashSet::new));
    }
}
