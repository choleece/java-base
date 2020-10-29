package cn.choleece.base.framework.spring.factorybean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author choleece
 * @Description: BeanConfiguration
 * @Date 2020-10-29 22:04
 **/
@Configuration
public class BeanConfiguration {
    @Bean
    public MyFactoryBean myFactoryBean() {
        MyFactoryBean factoryBean = new MyFactoryBean();
        factoryBean.setInterfaceName(IUserService.class.getName());
        factoryBean.setTarget(new UserServiceImpl());
        return factoryBean;
    }
}
