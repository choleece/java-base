package cn.choleece.base.framework.spring.environment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-12-17 23:02
 **/
@SpringBootApplication
public class ApplicationTest {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ApplicationTest.class, args);
        String username = context.getEnvironment().getProperty("jdbc.root.user");
        String password = context.getEnvironment().getProperty("jdbc.root.password");
        System.out.println("username===" + username);
        System.out.println("password===" + password);
        context.close();
    }

}
