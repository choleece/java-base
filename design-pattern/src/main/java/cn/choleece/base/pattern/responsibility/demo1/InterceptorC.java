package cn.choleece.base.pattern.responsibility.demo1;

/**
 * @author choleece
 * @Description: interceptor C
 * @Date 2020-09-16 21:38
 **/
public class InterceptorC implements Interceptor {

    @Override
    public Object handler(Object object) {
        System.out.println("interceptor C");
        return object;
    }
}
