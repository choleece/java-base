package cn.choleece.base.pattern.responsibility.demo1;

/**
 * @author choleece
 * @Description: 抽象拦截器
 * @Date 2020-09-16 21:35
 **/
public interface Interceptor {

    /**
     * handler
     * @param object object
     * @return the original object
     */
    Object handler(Object object);

}
