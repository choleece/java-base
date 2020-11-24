package cn.choleece.base.framework.spring.ioc;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * @description: TODO
 * @author: choleece
 * @time: 2019-11-19 14:22
 */
public class HelloWorldBean implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("我这里是BeanFactoryPostProcessor里的postProcessBeanFactory方法");
    }

    private String name;

    public void sayHello() {
        System.out.println("hello, " + name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class Hello implements BeanFactoryPostProcessor {

        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
            System.out.println("***");
        }

        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void sayHello() {
            System.out.println("inner static class, hello " + name);
        }
    }

    public void init() {
        System.out.println("初始化完成后...");
    }

    public void destroy() throws Exception {
        System.out.println("bean销毁后...");
    }
}
