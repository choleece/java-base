package cn.choleece.base.framework.spring.ioc;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * @author choleece
 * @Description: Factory Bean
 * @Date 2020-11-24 21:01
 **/
public class FactoryBeanClient implements FactoryBean<HelloWorldBean> {

    @Override
    public HelloWorldBean getObject() throws Exception {
        HelloWorldBean worldBean = new HelloWorldBean();
        worldBean.setName("factory bean");
        System.out.println("在这里注册bean的时候，也会将此bean注册进去，beanName是FactoryBean的BeanName前面加上&");
        return worldBean;
    }

    @Override
    public Class<?> getObjectType() {
        return HelloWorldBean.class;
    }

    public static void main(String[] args) {
        RootBeanDefinition beanDefinition = new RootBeanDefinition(FactoryBeanClient.class);
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerBeanDefinition("factoryBean", beanDefinition);

        FactoryBeanClient client = (FactoryBeanClient) beanFactory.getBean("&factoryBean");
        System.out.println(client);

        HelloWorldBean helloWorldBean = (HelloWorldBean) beanFactory.getBean("factoryBean");
        helloWorldBean.sayHello();
    }
}
