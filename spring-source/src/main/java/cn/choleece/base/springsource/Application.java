package cn.choleece.base.springsource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-12-17 23:19
 **/
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        System.out.println(context.getEnvironment().getProperty("name"));

        System.setProperty("sex", "Male");

        System.out.println(System.getProperty("sex"));

        System.out.println(context.getEnvironment().getProperty("sex"));
        context.close();
    }
    
}
