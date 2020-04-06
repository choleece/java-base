package cn.choleece.base.pattern.state;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-04-06 19:19
 **/
public class ClientTest {

    public static void main(String[] args) {
        RaffleActivity activity = new RaffleActivity(1);

        for (int i = 0; i < 30; i++) {

            System.out.println("第" + (i + 1) + "次抽奖");

            activity.deduce();

            activity.raffle();
        }
    }

}
