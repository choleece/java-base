package cn.choleece.base.framework.spring.factorybean;

import cn.choleece.base.framework.spring.ioc.HelloWorldBean;
import cn.choleece.base.framework.spring.ioc.JavaConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author choleece
 * @Description: MyFactoryBeanTest
 * @Date 2020-10-29 22:03
 **/
public class MyFactoryBeanTest {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeanConfiguration.class);

        IUserService javaBean = (IUserService) applicationContext.getBean("myFactoryBean");
        javaBean.printName();

        System.out.println(applicationContext.getBean("&myFactoryBean"));
    }

}
