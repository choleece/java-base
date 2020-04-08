package cn.choleece.base.framework.hystrix.custom.cb;

import cn.choleece.base.framework.hystrix.custom.state.CloseState;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-04-09 00:23
 **/
public class LocalCircuitBreaker extends AbstractCircuitBreaker {

    public LocalCircuitBreaker(String failRateForClose,
                               int idleTimeForOpen,
                               String passRateForHalfOpen, int failNumForHalfOpen){
        this.thresholdFailRateForClose = failRateForClose;
        this.thresholdIdleTimeForOpen = idleTimeForOpen;
        this.thresholdPassRateForHalfOpen = passRateForHalfOpen;
        this.thresholdFailNumForHalfOpen = failNumForHalfOpen;
    }

    @Override
    public void reset() {
        this.setCurrentState(new CloseState());
    }

    @Override
    public boolean canPassCheck() {
        return getCurrentState().canPassCheck(this);
    }

    @Override
    public void countFailNum() {
        getCurrentState().countFailNum(this);
    }
}
