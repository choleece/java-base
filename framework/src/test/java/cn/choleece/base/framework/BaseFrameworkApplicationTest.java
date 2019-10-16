package cn.choleece.base.framework;

import cn.choleece.base.framework.config.BeanTest;
import cn.choleece.base.framework.template.CusRedisTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-09-22 22:07
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseFrameworkApplicationTest {
    @Autowired
    private BeanTest beanTest;
    @Autowired
    private CusRedisTemplate redisTemplate;

    @Test
    public void testAutoConfig() {
        System.out.println(beanTest.toString());

        System.out.println(redisTemplate.toString());

        redisTemplate.opsForValue().set("name", "chaoli");

        System.out.println(redisTemplate.opsForValue().get("name"));
    }
}
