package cn.choleece.base.framework.spring.ioc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 基于Java配置
 * @author: choleece
 * @time: 2019-11-19 17:53
 */
@Configuration
public class JavaConfig {

    @Bean
    public HelloWorldBean helloWorldBean() {
        return new HelloWorldBean();
    }
}
