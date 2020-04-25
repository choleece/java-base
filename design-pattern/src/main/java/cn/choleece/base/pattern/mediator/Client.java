package cn.choleece.base.pattern.mediator;

/**
 * @author choleece
 * @Description: 中介者模式， 利用中介行为，保证各应用组件直接互不交互，统一通过中介者进行调度
 * @Date 2020-04-25 15:52
 **/
public class Client {


    public static void main(String[] args) {
        Mediator mediator = new ConcreteMediator();
        Alarm alarm = new Alarm("alarm", mediator);
        TV tv = new TV("tv", mediator);
        System.out.println("我要打开闹钟了...");
        alarm.startAlarm();
        alarm.sendMessage(1);

        System.out.println("我要打开电视了...");
        tv.startTV();
        tv.sendMessage(1);
    }
}
