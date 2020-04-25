package cn.choleece.base.miaosha.common;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-04-25 22:48
 **/
@SpringBootApplication
@ComponentScan(basePackages = "cn.choleece.base.miaosha")
@MapperScan(basePackages = "cn.choleece.base.miaosha.*.mapper")
public class CommonApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommonApplication.class, args);
    }

}
