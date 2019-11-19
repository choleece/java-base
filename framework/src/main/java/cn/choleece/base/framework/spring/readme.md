# Spring 执行流程
## Spring IOC配置方式大致可以有三种
1. 基于XML的配置
```
<bean id="innerHello" class="cn.choleece.base.framework.spring.ioc.HelloWorldBean.Hello"/>
``` 
2. 基于Java的方式
```
@Configuration
public class Config {
    
    @Bean
    public HelloWorldBean helloWorldBean() {
        return new HelloWorldBean();
    }
}
```
3. 基于注解的形式

## bean 的定义