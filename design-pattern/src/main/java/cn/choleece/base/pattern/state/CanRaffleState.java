package cn.choleece.base.pattern.state;

import java.util.Random;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-04-06 19:00
 **/
public class CanRaffleState extends State {

    public RaffleActivity activity;

    public CanRaffleState(RaffleActivity activity) {
        this.activity = activity;
    }

    @Override
    void deduceMoney() {
        System.out.println("您已经扣除过积分了，不需要再进行扣除");
    }

    @Override
    boolean raffle() {
        Random random = new Random();
        int val = random.nextInt(10);

        if (val == 0) {
            System.out.println("恭喜你，抽中了");
            activity.setCurrentState(activity.getDispenseState());
            return true;
        }
        System.out.println("很遗憾，您没有抽中，下次好运");
        activity.setCurrentState(activity.getNoRaffleState());
        return false;
    }

    @Override
    void dispensePrize() {
        System.out.println("很遗憾，您还没有抽中，请继续");
    }
}
