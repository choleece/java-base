package cn.choleece.base.pattern.strategy;

/**
 * @author choleece
 * @Description: 具体策略（Concrete Strategy）类：实现了抽象策略定义的接口，提供具体的算法实现。
 * @Date 2019-09-15 13:14
 **/
public class ConcreteStrategyTwo implements Strategy {

    @Override
    public void strategyMethod() {
        System.out.println("策略2");
    }
}
