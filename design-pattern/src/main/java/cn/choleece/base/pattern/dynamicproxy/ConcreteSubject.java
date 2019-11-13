package cn.choleece.base.pattern.dynamicproxy;

/**
 * 必须实现一个接口
 * @author choleece
 */
public class ConcreteSubject implements Subject {

    @Override
    public void request() {
        System.out.println("request something...");
    }

    @Override
    public void response() {
        System.out.println("这是我的第二个方法...");
    }
}
