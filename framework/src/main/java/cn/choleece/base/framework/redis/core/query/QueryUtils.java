package cn.choleece.base.framework.redis.core.query;

import cn.choleece.base.framework.redis.connection.DefaultSortParameters;
import cn.choleece.base.framework.redis.connection.SortParameters;
import cn.choleece.base.framework.redis.serializer.RedisSerializer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-15 22:55
 **/
public abstract class QueryUtils {

    public QueryUtils() {
    }

    public static <K> SortParameters convertQuery(SortQuery<K> query, RedisSerializer<String> stringSerializer) {
        return new DefaultSortParameters(stringSerializer.serialize(query.getBy()), query.getLimit(), serialize(query.getGetPattern(), stringSerializer), query.getOrder(), query.isAlphabetic());
    }

    private static byte[][] serialize(List<String> strings, RedisSerializer<String> stringSerializer) {
        List<byte[]> raw = null;
        if (strings == null) {
            raw = Collections.emptyList();
        } else {
            raw = new ArrayList(strings.size());
            Iterator var3 = strings.iterator();

            while(var3.hasNext()) {
                String key = (String)var3.next();
                ((List)raw).add(stringSerializer.serialize(key));
            }
        }

        return (byte[][])((List)raw).toArray(new byte[((List)raw).size()][]);
    }
}
