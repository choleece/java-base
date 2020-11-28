package cn.choleece.base.pattern.responsibility.demo2;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author choleece
 * @Description: 类似于filter chain
 * @Date 2020-09-16 21:39
 **/
public class InterceptorChain {

    private List<Interceptor> interceptors = new LinkedList<>();

    private Iterator<Interceptor> iterator;

    public void addInterceptor(Interceptor interceptor) {
        interceptors.add(interceptor);
    }

    public Object plugin(Object object) {

        // iterator一旦实例化后，后续再对集合所做的增加，修改，然后再去操作iterator后就会报错
        if (iterator == null) {
            iterator = interceptors.iterator();
        }
        if (iterator.hasNext()) {
            Interceptor next = iterator.next();
            object = next.handler(object, this);
        }

        return object;
    }
}
