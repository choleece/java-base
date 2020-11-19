package cn.choleece.base.framework.spring.annotation;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author choleece
 * @Description: @Import 注解的使用场景
 * @Date 2020-11-19 21:59
 **/
public class ImportTest {

    public static void main(String[] args) {
        // 这里的参数代表要做操作的类

        // 第一种方式
        // AnnotationConfigApplicationContext applicationContext=new AnnotationConfigApplicationContext(ImportTypeOne.class);
        // 第二、三种方式
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeanImport.class);

        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String name : beanDefinitionNames) {
            System.out.println(name);
        }
    }

}
