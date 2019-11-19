package cn.choleece.base.framework.spring.ioc;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 基于Java配置
 * @author: choleece
 * @time: 2019-11-19 17:53
 */
@Configuration
public class JavaConfig implements InitializingBean, DisposableBean {

    @Bean(value = "helloWorldBean", initMethod = "init", destroyMethod = "destroy")
    public HelloWorldBean helloWorldBean() {
        return new HelloWorldBean();
    }

    public void init() {
        System.out.println("init");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("初始化完成后...");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("bean销毁后...");
    }
}
