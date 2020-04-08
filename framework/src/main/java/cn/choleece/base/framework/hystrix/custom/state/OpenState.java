package cn.choleece.base.framework.hystrix.custom.state;

import cn.choleece.base.framework.hystrix.custom.cb.AbstractCircuitBreaker;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-04-09 00:03
 **/
public class OpenState implements CircuitBreakerState {

    private long startTime = System.currentTimeMillis();

    @Override
    public String getCurrentStateName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void checkAndSwitchState(AbstractCircuitBreaker acb) {
        // 打开状态，检查是否已达到时间，如果到了，就需要切换到半打开状态
        long now = System.currentTimeMillis();
        long idleTime = acb.thresholdIdleTimeForOpen * 1000L;
        if (startTime + idleTime <= now) {
            acb.setCurrentState(new HalfCloseState());
        }
    }

    @Override
    public boolean canPassCheck(AbstractCircuitBreaker acb) {
        // 检查，是否需要切换状态
        checkAndSwitchState(acb);
        return false;
    }

    @Override
    public void countFailNum(AbstractCircuitBreaker acb) {
        // do nothing
    }
}
