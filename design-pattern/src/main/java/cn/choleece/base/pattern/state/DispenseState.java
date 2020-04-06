package cn.choleece.base.pattern.state;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-04-06 19:02
 **/
public class DispenseState extends State {

    private RaffleActivity activity;

    public DispenseState(RaffleActivity activity) {
        this.activity = activity;
    }

    @Override
    void deduceMoney() {
        System.out.println("您已中奖，无需再扣减积分");
    }

    @Override
    boolean raffle() {
        System.out.println("您已中奖，请先兑换");
        return false;
    }

    @Override
    void dispensePrize() {
        if (activity.getCount() > 0) {
            System.out.println("恭喜您，领奖成功!!!");
            activity.setCurrentState(activity.getNoRaffleState());
        } else {
            System.out.println("很遗憾，奖品已领取完成");
            activity.setCurrentState(activity.getDispenseOutState());
        }
    }
}
