package cn.choleece.base.pattern.state;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-04-06 18:44
 **/
public abstract class State {

    /**
     * 扣除积分
     */
    abstract void deduceMoney();

    /**
     * 可以领奖
     * @return
     */
    abstract boolean raffle();

    /**
     * 领奖
     */
    abstract void dispensePrize();
}
