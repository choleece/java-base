package cn.choleece.base.pattern.strategy;

/**
 * @author choleece
 * @Description: 象策略（Strategy）类：定义了一个公共接口，各种不同的算法以不同的方式实现这个接口，环境角色使用这个接口调用不同的算法，一般使用接口或抽象类实现
 * @Date 2019-09-15 13:13
 **/
public interface Strategy {

    void strategyMethod();

}
