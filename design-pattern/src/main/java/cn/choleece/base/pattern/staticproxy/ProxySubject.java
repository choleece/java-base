package cn.choleece.base.pattern.staticproxy;

/**
 * 代理对象
 * @author choleece
 */
public class ProxySubject implements Subject {

    private Subject realSubject;

    @Override
    public void request() {
        if (realSubject == null) {
            realSubject = new RealSubject();
        }

        preRequest();

        realSubject.request();

        postRequest();
    }

    public void preRequest() {
        System.out.println("在请求方法前做一些事情");
    }

    public void postRequest() {
        System.out.println("在请求方法后做一些事情");
    }
}
