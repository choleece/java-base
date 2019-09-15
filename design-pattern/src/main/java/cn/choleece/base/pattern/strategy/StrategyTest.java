package cn.choleece.base.pattern.strategy;

/**
 * @author choleece
 * @Description: 策略模式测试
 * @Date 2019-09-15 13:16
 **/
public class StrategyTest {

    public static void main(String[] args) {
        // 策略上下文
        StrategyContext context = new StrategyContext();

        // 设置策略
        context.setStrategy(new ConcreteStrategyOne());

        // 执行具体策略
        context.getStrategy().strategyMethod();

        // 重新设置策略
        context.setStrategy(new ConcreteStrategyTwo());

        // 执行具体策略
        context.getStrategy().strategyMethod();
    }

}
