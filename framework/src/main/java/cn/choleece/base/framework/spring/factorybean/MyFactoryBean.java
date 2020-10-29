package cn.choleece.base.framework.spring.factorybean;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author choleece
 * @Description: 测试factory bean
 * @Date 2020-10-29 21:51
 * 参考： https://www.cnblogs.com/aspirant/p/9082858.html
 **/
public class MyFactoryBean implements FactoryBean<IUserService>, InitializingBean, DisposableBean {

    private String interfaceName;
    private Object target;
    private IUserService proxyObj;

    @Override
    public void destroy() throws Exception {
        System.out.println("DisposableBean destroy invoke");
    }

    @Override
    public IUserService getObject() throws Exception {
        return proxyObj;
    }

    @Override
    public Class<?> getObjectType() {
        return proxyObj.getClass();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("InitializingBean afterPropertiesSet invoke");

        proxyObj = (IUserService) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{Class.forName(interfaceName)}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println(proxy.getClass());
                System.out.println("before invoke target method...");
                Object result = method.invoke(target, args);
                System.out.println("after invoke target method...");
                return result;
            }
        });
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }
}
