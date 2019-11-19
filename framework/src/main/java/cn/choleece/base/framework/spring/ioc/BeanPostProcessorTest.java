package cn.choleece.base.framework.spring.ioc;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author choleece
 * @Description: Bean 后置器
 * @Date 2019-11-19 22:30
 **/
public class BeanPostProcessorTest {

    public static void main(String[] args) {
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

        HelloWorld helloWorld = (HelloWorld) context.getBean("helloWorld");

        helloWorld.printMessage();

        context.registerShutdownHook();
    }

    static class HelloWorld implements BeanPostProcessor, InitializingBean, DisposableBean {

        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void printMessage() {
            System.out.println(message);
        }

        @PostConstruct
        public void postConstruct() {
            System.out.println("post construct...");
        }

        @Override
        public void afterPropertiesSet() throws Exception {
            System.out.println("afterPropertiesSet...");
        }

        /**
         * 这里也可以去实现InitializingBean 或者使用@PostContructor注解
         */
        public void init() {
            System.out.println("init...");
        }

        @PreDestroy
        public void preDestroy() {
            System.out.println("pre destroy...");
        }

        /**
         * 这里也可以去实现DisposalBean 或者使用@PreDestroy注解
         */
        public void destroyMethod() {
            System.out.println("destroy method...");
        }

        /**
         * 在初始化之前做某些事情
         * @param bean
         * @param beanName
         * @return
         * @throws BeansException
         */
        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            System.out.println("postProcessBeforeInitialization...");
            return null;
        }

        /**
         * 在初始化之后做某些事情
         * @param bean
         * @param beanName
         * @return
         * @throws BeansException
         */
        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            System.out.println("postProcessAfterInitialization....");
            return null;
        }

        @Override
        public void destroy() throws Exception {
            System.out.println("destroy....");
        }
    }
}
