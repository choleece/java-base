package cn.choleece.base.pattern.staticproxy;

/**
 * @author choleece
 */
public class RealSubject implements Subject {

    public void request() {
        System.out.println("我是一个真是的方法");
    }
}
