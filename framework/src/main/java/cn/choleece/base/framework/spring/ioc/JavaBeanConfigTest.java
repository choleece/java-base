package cn.choleece.base.framework.spring.ioc;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @description: TODO
 * @author: choleece
 * @time: 2019-11-19 18:07
 */
public class JavaBeanConfigTest {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(JavaConfig.class);

        HelloWorldBean javaBean = (HelloWorldBean) applicationContext.getBean("helloWorldBean");
        javaBean.sayHello();

        JavaConfig javaConfig = applicationContext.getBean(JavaConfig.class);
        javaConfig.init();
    }

}
