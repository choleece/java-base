package cn.choleece.base.pattern.dynamicproxy;

/**
 * 动态代理测试
 * @author choleece
 */
public class DynamicProxyTest {

    public static void main(String[] args) {
        LogHandler logHandler = new LogHandler();

        Subject subject = (Subject) logHandler.newProxyInstance(new ConcreteSubject());

        subject.request();
    }

}
