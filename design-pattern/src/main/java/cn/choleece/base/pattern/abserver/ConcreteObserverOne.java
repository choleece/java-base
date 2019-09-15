package cn.choleece.base.pattern.abserver;

/**
 * 具体的观察者，用于对被观察者变化作出相应的反应
 * 具体观察者（Concrete Observer）角色：实现抽象观察者中定义的抽象方法，以便在得到目标的更改通知时更新自身的状态。
 * @author choleece
 */
public class ConcreteObserverOne implements Observer {

    @Override
    public void update(String msg) {
        System.out.println("observer one receive msg: " + msg);
    }
}
