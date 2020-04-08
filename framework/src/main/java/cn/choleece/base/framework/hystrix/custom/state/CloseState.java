package cn.choleece.base.framework.hystrix.custom.state;

import cn.choleece.base.framework.hystrix.custom.cb.AbstractCircuitBreaker;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author choleece
 * @Description: 断路器关闭状态
 * @Date 2020-04-08 23:49
 **/
public class CloseState implements CircuitBreakerState {

    /**
     * 进入当前状态的时间
     */
    private long stateTime = System.currentTimeMillis();

    /**
     * 统计失败的次数
     */
    private AtomicInteger failNum = new AtomicInteger(0);

    private long failNumClearTime = System.currentTimeMillis();

    @Override
    public String getCurrentStateName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void checkAndSwitchState(AbstractCircuitBreaker acb) {

        // 失败次数达到阈值，切换状态到打开状态
        long maxFailNum = Long.valueOf(acb.thresholdFailRateForClose.split("/")[0]);
        if (failNum.get() >= maxFailNum) {
            acb.setCurrentState(new OpenState());
        }
    }

    @Override
    public boolean canPassCheck(AbstractCircuitBreaker acb) {
        // 关闭状态下，都允许通过
        return true;
    }

    @Override
    public void countFailNum(AbstractCircuitBreaker acb) {
        // 检查计数器是否过期，否则需要重新计数
        long period = Long.valueOf(acb.thresholdFailRateForClose.split("/")[1]) * 1000;

        long now = System.currentTimeMillis();
        if (failNumClearTime + period <= now) {
            failNum.set(0);
        }

        // 失败次数+1
        failNum.incrementAndGet();

        // 检查是否需要切换状态
        checkAndSwitchState(acb);
    }
}
