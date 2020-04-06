package cn.choleece.base.pattern.state;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-04-06 18:57
 **/
public class NoRaffleState extends State {

    private RaffleActivity activity;

    public NoRaffleState(RaffleActivity activity) {
        this.activity = activity;
    }

    @Override
    void deduceMoney() {
        System.out.println("扣除50积分成功");
        activity.setCurrentState(activity.getCanRaffleState());
    }

    @Override
    boolean raffle() {
        System.out.println("当前还不能抽奖");
        return false;
    }

    @Override
    void dispensePrize() {
        System.out.println("您还未中奖，还不能进行抽奖");
    }
}
