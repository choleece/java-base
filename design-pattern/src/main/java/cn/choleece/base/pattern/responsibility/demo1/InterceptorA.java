package cn.choleece.base.pattern.responsibility.demo1;

/**
 * @author choleece
 * @Description: interceptor A
 * @Date 2020-09-16 21:37
 **/
public class InterceptorA implements Interceptor {

    @Override
    public Object handler(Object object) {
        System.out.println("interceptor A");
        return object;
    }
}
