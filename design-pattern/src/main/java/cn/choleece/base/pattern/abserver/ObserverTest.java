package cn.choleece.base.pattern.abserver;

/**
 * 观察者模式测试
 * 当被观察者发生变化时，主动通知观察者队列的观察者，让其根据自己的实现逻辑进行相应的变化
 * @author choleece
 */
public class ObserverTest {

    public static void main(String[] args) {
        ConcreteSubject concreteSubject = new ConcreteSubject();

        // 向观察者队列里添加观察者
        concreteSubject.addObserver(new ConcreteObserverOne());
        concreteSubject.addObserver(new ConcreteObserverTwo());

        // 向观察者发送消息
        concreteSubject.notifyObservers();

        // 当被观察者内容发生变化后，通知所有对观察者
        concreteSubject.changeMsg("hello, every one,my msg will be changed!!!");
    }

}
