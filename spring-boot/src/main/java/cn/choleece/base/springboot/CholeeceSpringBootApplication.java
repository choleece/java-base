package cn.choleece.base.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-09-22 22:04
 **/
@EnableRetry
@SpringBootApplication
public class CholeeceSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(CholeeceSpringBootApplication.class, args);
    }

}
