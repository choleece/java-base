package cn.choleece.base.framework.spring.ioc;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.GenericApplicationContext;

/**
 * @author choleece
 * @Description: bean definition 定义
 * @Date 2020-09-26 12:39
 **/
public class BeanDefinitionTest {

    public static void main(String[] args) {
        // 新建一个工厂
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();

        // 新建一个 bean definition
        GenericBeanDefinition beanDefinition = (GenericBeanDefinition) BeanDefinitionBuilder
                .genericBeanDefinition(HelloWorldBean.class)
                .setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE)
                .getBeanDefinition();

        // 注册到工厂
        factory.registerBeanDefinition("helloWorld", beanDefinition);

        // 自己定义一个 bean post processor. 作用是在 bean 初始化之后, 判断这个 bean 如果实现了 ApplicationContextAware 接口, 就把 context 注册进去..(先不要管 context 哪来的...例子嘛)
        factory.addBeanPostProcessor(new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                return bean;
            }

            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof ApplicationContextAware) {
                    GenericApplicationContext context = new GenericApplicationContext(factory);
                    ((ApplicationContextAware) bean).setApplicationContext(context);
                }
                return bean;
            }
        });

        // 再注册一个 bean post processor: AutowiredAnnotationBeanPostProcessor. 作用是搜索这个 bean 中的 @Autowired 注解, 生成注入依赖的信息.
        AutowiredAnnotationBeanPostProcessor autowiredAnnotationBeanPostProcessor = new AutowiredAnnotationBeanPostProcessor();
        autowiredAnnotationBeanPostProcessor.setBeanFactory(factory);
        factory.addBeanPostProcessor(autowiredAnnotationBeanPostProcessor);

        HelloWorldBean helloWorldBean = factory.getBean("helloWorld", HelloWorldBean.class);
        helloWorldBean.sayHello();
    }

}
