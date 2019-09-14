package cn.choleece.base.pattern.staticproxy;

/**
 * 利用代理对象进行操作 此类方式为静态代理
 * @author choleece
 */
public class ProxyTest {

    public static void main(String[] args) {
        ProxySubject proxySubject = new ProxySubject();

        proxySubject.request();

    }

}
