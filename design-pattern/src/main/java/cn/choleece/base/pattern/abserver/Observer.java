package cn.choleece.base.pattern.abserver;

/**
 * 抽象观察者（Observer）角色：它是一个抽象类或接口，它包含了一个更新自己的抽象方法，当接到具体主题的更改通知时被调用
 * @author choleece
 */
public interface Observer {

    void update(String msg);

}
