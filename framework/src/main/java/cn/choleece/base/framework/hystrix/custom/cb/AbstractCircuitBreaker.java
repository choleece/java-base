package cn.choleece.base.framework.hystrix.custom.cb;

import cn.choleece.base.framework.hystrix.custom.state.CircuitBreakerState;
import cn.choleece.base.framework.hystrix.custom.state.CloseState;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-04-08 23:48
 **/
public abstract class AbstractCircuitBreaker implements CircuitBreaker {

    private volatile CircuitBreakerState currentState = new CloseState();

    /**
     * 在熔断器关闭的情况下，在多少秒内多少次失败进入，熔断器打开(默认10分钟内10次失败)
     */
    public String thresholdFailRateForClose = "10/600";

    /**
     * 在熔断器打开情况下，熔断器多少秒进入半开状态
     */
    public int thresholdIdleTimeForOpen = 1800;

    /**
     * 在半开状态下，在多长时间内放多少次请求去试探(默认，10分钟内10次)
     */
    public String thresholdPassRateForHalfOpen = "10/600";

    /**
     * 在半开状态下，失败超过多少次，重新进入开启状态，否则进入关闭状态
     */
    public int thresholdFailNumForHalfOpen = 1;

    public void setCurrentState(CircuitBreakerState currentState) {
        // 当前状态，不用作切换
        CircuitBreakerState curState = getCurrentState();
        if (curState.getCurrentStateName().equals(currentState.getCurrentStateName())) {
            return;
        }

        synchronized (this) {
            curState = getCurrentState();
            if (curState.getCurrentStateName().equals(currentState.getCurrentStateName())) {
                return;
            }

            this.currentState = currentState;
            System.out.println("熔断器状态转移为:" + curState.getCurrentStateName() + "-->" + currentState.getCurrentStateName());
        }
    }

    public CircuitBreakerState getCurrentState() {
        return currentState;
    }
}
