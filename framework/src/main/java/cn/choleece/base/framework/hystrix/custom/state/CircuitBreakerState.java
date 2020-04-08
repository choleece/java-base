package cn.choleece.base.framework.hystrix.custom.state;

import cn.choleece.base.framework.hystrix.custom.cb.AbstractCircuitBreaker;

/**
 * @author choleece
 * @Description: 熔断器状态
 * @Date 2020-04-08 23:46
 **/
public interface CircuitBreakerState {

    /**
     * 获取当前状态的名称
     * @return
     */
    String getCurrentStateName();

    /**
     * 检查并扭转当前状态
     * @param acb
     */
    void checkAndSwitchState(AbstractCircuitBreaker acb);

    /**
     * 是否可以通过断路器
     * @param acb
     * @return
     */
    boolean canPassCheck(AbstractCircuitBreaker acb);

    /**
     * 统计失败次数
     * @param acb
     */
    void countFailNum(AbstractCircuitBreaker acb);
}
