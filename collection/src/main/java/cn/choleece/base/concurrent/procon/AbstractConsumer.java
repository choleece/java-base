package cn.choleece.base.concurrent.procon;

/**
 * Created by choleece on 2019/6/22.
 */
public abstract class AbstractConsumer implements Consumer, Runnable {

    @Override
    public void run() {
        while (true) {
            try {
                consume();
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
