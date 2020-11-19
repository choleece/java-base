package cn.choleece.base.framework.spring.annotation;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author choleece
 * @Description: 第三种形式，以实现ImportBeanDefinitionRegistrar的形式， 来手动决定注入什么，并且可在注入的时候进行某种操作
 * @Date 2020-11-19 22:03
 **/
public class ImportTypeThree implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // 指定bean定义信息（包括bean的类型、作用域...）
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(BaseClassOne.class);
        // 注册一个bean指定bean名字（id）
        registry.registerBeanDefinition("customBean", rootBeanDefinition);
    }
}
