package cn.choleece.base.framework.mybatis.spring;

import cn.choleece.base.framework.mybatis.config.MybatisConfig;
import cn.choleece.base.framework.mybatis.entity.SysUser;
import cn.choleece.base.framework.mybatis.mapper.SysUserMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

/**
 * @author choleece
 * @Description: Spring Mybatis整合
 * @Date 2019-11-20 22:31
 **/
public class SpringMybatisTest {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(MybatisConfig.class);

        SysUserMapper cityMapper = (SysUserMapper) applicationContext.getBean("sysUserMapper");

        List<SysUser> cities = cityMapper.listUsers();

        System.out.println(cities);
    }
}
