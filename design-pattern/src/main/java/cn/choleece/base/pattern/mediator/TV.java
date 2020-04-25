package cn.choleece.base.pattern.mediator;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-04-25 16:20
 **/
public class TV extends Colleague {

    public TV(String name, Mediator mediator) {
        super(name, mediator);

        mediator.register(name, this);
    }

    public void startTV() {
        System.out.println("闹钟关了，我要开启电视了");
    }

    public void stopTV() {
        System.out.println("闹钟开了，我要关闭电视了");
    }
}
