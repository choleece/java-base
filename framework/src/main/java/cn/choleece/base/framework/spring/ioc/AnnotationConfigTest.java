package cn.choleece.base.framework.spring.ioc;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @description: TODO
 * @author: choleece
 * @time: 2019-11-19 18:19
 */
public class AnnotationConfigTest {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(HelloWorldComponent.class);

        HelloWorldComponent helloWorldComponent = (HelloWorldComponent) applicationContext.getBean("hello");
        helloWorldComponent.sayHello();
    }

}
