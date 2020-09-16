package cn.choleece.base.pattern.responsibility.demo1;

import java.util.LinkedList;
import java.util.List;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-09-16 21:39
 **/
public class InterceptorChain {

    List<Interceptor> interceptors = new LinkedList<>();

    public void addInterceptor(Interceptor interceptor) {
        interceptors.add(interceptor);
    }

    public Object pluginAll(Object object) {
        for (Interceptor interceptor : interceptors) {
            object = interceptor.handler(object);
        }
        return object;
    }
}
