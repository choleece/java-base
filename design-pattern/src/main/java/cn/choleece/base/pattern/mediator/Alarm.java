package cn.choleece.base.pattern.mediator;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-04-25 16:28
 **/
public class Alarm extends Colleague {

    public Alarm(String name, Mediator mediator) {
        super(name, mediator);

        mediator.register(name, this);
    }

    public void startAlarm() {
        System.out.println("闹钟关了，我要开启电视了");
    }

    public void stopAlarm() {
        System.out.println("闹钟开了，我要关闭电视了");
    }
}
