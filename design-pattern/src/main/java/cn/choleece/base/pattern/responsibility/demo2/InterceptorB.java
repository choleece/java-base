package cn.choleece.base.pattern.responsibility.demo2;

/**
 * @author choleece
 * @Description: interceptor B
 * @Date 2020-09-16 21:38
 **/
public class InterceptorB implements Interceptor {

    @Override
    public Object handler(Object target, InterceptorChain chain) {
        System.out.println("interceptor B");
        return target;
    }
}
