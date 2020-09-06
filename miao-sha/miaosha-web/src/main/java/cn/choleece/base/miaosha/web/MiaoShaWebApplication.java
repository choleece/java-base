package cn.choleece.base.miaosha.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-04-25 23:45
 **/
@SpringBootApplication
@ComponentScan(basePackages = "cn.choleece.base")
@MapperScan(basePackages = "cn.choleece.base.miaosha.*.mapper")
public class MiaoShaWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiaoShaWebApplication.class, args);
    }
}
