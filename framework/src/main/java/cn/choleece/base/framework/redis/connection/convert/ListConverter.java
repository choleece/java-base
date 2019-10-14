package cn.choleece.base.framework.redis.connection.convert;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-14 22:25
 **/
public class ListConverter<S, T> implements Converter<List<S>, List<T>> {
    Converter<S, T> itemConverter;

    public ListConverter(Converter<S, T> itemConverter) {
        this.itemConverter = itemConverter;
    }

    @Override
    public List<T> convert(List<S> source) {
        List<T> results = new ArrayList(source.size());
        Iterator var3 = source.iterator();

        while(var3.hasNext()) {
            S result = (S) var3.next();
            results.add(this.itemConverter.convert(result));
        }

        return results;
    }
}
