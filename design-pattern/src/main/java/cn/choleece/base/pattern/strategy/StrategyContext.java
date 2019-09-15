package cn.choleece.base.pattern.strategy;

/**
 * @author choleece
 * @Description: 环境（Context）类：持有一个策略类的引用，最终给客户端调用。
 * @Date 2019-09-15 13:15
 **/
public class StrategyContext {

    private Strategy strategy;

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }
}
