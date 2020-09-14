package cn.choleece.base.pattern.dynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-09-14 23:01
 **/
public class ProxyTest {

    interface Interface {
        /**
         * 执行某个方法
         */
        void execute();
    }

    public static void main(String[] args) {
        Interface in = (Interface) Proxy.newProxyInstance(ProxyTest.class.getClassLoader(), new Class[]{Interface.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("调用了invoke方法");
                return "";
            }
        });

        in.execute();
    }

}
