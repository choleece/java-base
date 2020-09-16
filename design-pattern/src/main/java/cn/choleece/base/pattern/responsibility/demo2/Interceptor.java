package cn.choleece.base.pattern.responsibility.demo2;

/**
 * @author choleece
 * @Description: 另外一种责任链的形式
 * @Date 2020-09-16 21:58
 **/
public interface Interceptor {

    /**
     * @param target target
     * @param chain chain
     * @return target after handle
     */
    Object handler(Object target, InterceptorChain chain);

}
