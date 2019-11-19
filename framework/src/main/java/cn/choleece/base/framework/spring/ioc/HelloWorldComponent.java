package cn.choleece.base.framework.spring.ioc;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @description: 基于注解等配置
 * @author: choleece
 * @time: 2019-11-19 18:12
 *
 * Component 指定bean的名字
 */
@Component("hello")
public class HelloWorldComponent {

    public void sayHello() {
        System.out.println("hello annotation");
    }

    @PostConstruct
    public void init() {
        System.out.println("init...");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("destroy...");
    }

}
