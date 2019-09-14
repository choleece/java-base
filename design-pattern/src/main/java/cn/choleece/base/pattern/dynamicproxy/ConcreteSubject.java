package cn.choleece.base.pattern.dynamicproxy;

/**
 * 必须实现一个接口
 * @author choleece
 */
public class ConcreteSubject implements Subject {

    public void request() {
        System.out.println("request something...");
    }
}
