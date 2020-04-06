package cn.choleece.base.pattern.state;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-04-06 19:02
 **/
public class DispenseOutState extends State {

    private RaffleActivity activity;

    public DispenseOutState(RaffleActivity activity) {
        this.activity = activity;
    }

    @Override
    void deduceMoney() {
        System.out.println("奖品已领取完成");
    }

    @Override
    boolean raffle() {
        System.out.println("奖品已领取完成");
        return false;
    }

    @Override
    void dispensePrize() {
        System.out.println("奖品已领取完成");
    }
}
