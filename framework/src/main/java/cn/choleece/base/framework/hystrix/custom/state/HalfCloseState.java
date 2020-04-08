package cn.choleece.base.framework.hystrix.custom.state;

import cn.choleece.base.framework.hystrix.custom.cb.AbstractCircuitBreaker;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-04-09 00:03
 **/
public class HalfCloseState implements CircuitBreakerState {

    private long startTime = System.currentTimeMillis();

    /**
     * 半开状态，失败计数器
     */
    private AtomicInteger failNum = new AtomicInteger(0);

    /**
     * 半开状态，允许通过计数器
     */
    private AtomicInteger passNum = new AtomicInteger(0);

    @Override
    public String getCurrentStateName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void checkAndSwitchState(AbstractCircuitBreaker acb) {
        long idleTime = Long.valueOf(acb.thresholdPassRateForHalfOpen.split("/")[1]) * 1000L;
        long now = System.currentTimeMillis();

        if (startTime + idleTime <= now) {
            // 如果半开状态已结束，失败次数已到阈值
            int maxFailNum = acb.thresholdFailNumForHalfOpen;
            if (failNum.get() >= maxFailNum) {
                // 失败超过阈值，被认为服务还未恢复，重新设置成开启状态
                acb.setCurrentState(new OpenState());
            } else {
                // 没有超过阈值，证明服务已恢复，可以设置为关闭状态
                acb.setCurrentState(new CloseState());
            }

        }
    }

    @Override
    public boolean canPassCheck(AbstractCircuitBreaker acb) {
        // 检查是否可以切换状态
        checkAndSwitchState(acb);

        // 超过了规定时间内的允许放过的值，那么不再对请求进行放行
        int maxPassNum = Integer.valueOf(acb.thresholdPassRateForHalfOpen.split("/")[0]);
        if (passNum.get() > maxPassNum) {
            return false;
        }

        return passNum.incrementAndGet() <= maxPassNum;
    }

    @Override
    public void countFailNum(AbstractCircuitBreaker acb) {
        // 失败次数+1
        failNum.incrementAndGet();

        // 检查是否可以切换状态
        checkAndSwitchState(acb);
    }
}
