package cn.choleece.base.concurrent.procon;

/**
 * Created by choleece on 2019/6/22.
 */
public interface Model {

    Runnable newConsumer();

    Runnable newProducer();

}
