package cn.choleece.base.framework.spring.ioc;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

/**
 * @description: 基于XML配置
 * @author: choleece
 * @time: 2019-11-19 18:06
 */
public class XmlConfigTest {

    public static void main(String[] args) {
        BeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource("beans.xml"));

        HelloWorldBean bean = (HelloWorldBean) beanFactory.getBean("hello");

        bean.sayHello();

        HelloWorldBean.Hello innerHello = (HelloWorldBean.Hello) beanFactory.getBean("innerHello");

        innerHello.sayHello();

        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

        HelloWorldBean contextBean = (HelloWorldBean) context.getBean("hello");
        contextBean.sayHello();

        HelloWorldBean.Hello innerContextHello = (HelloWorldBean.Hello) context.getBean("innerHello");

        innerContextHello.sayHello();
    }
}
