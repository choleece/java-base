package cn.choleece.base.pattern.responsibility.demo2;

/**
 * @author choleece
 * @Description: interceptor C
 * @Date 2020-09-16 21:38
 **/
public class InterceptorC implements Interceptor {

    @Override
    public Object handler(Object target, InterceptorChain chain) {
        System.out.println("interceptor C");
        return target;
    }
}
