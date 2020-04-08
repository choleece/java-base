package cn.choleece.base.framework.hystrix.custom.cb;

/**
 * @author choleece
 * @Description: 熔断器
 * @Date 2020-04-08 23:44
 **/
public interface CircuitBreaker {

    /**
     * 重置熔断器
     */
    void reset();

    /**
     * 是否允许通过熔断器
     * @return
     */
    boolean canPassCheck();

    /**
     * 统计失败次数
     */
    void countFailNum();
}
