package cn.choleece.base.concurrent.procon;

/**
 * Created by choleece on 2019/6/22.
 */
public abstract class AbstractProducer implements Producer, Runnable {
    @Override
    public void run() {
        while (true) {
            try {
                produce();
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
