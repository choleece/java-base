package cn.choleece.base.springboot;

import cn.choleece.base.springboot.autoconfig.redis.properties.RedisConfig;
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
public class CholeeceSpringBootApplicationTest {
    @Autowired
    private RedisConfig redisConfig;

    @Test
    public void testAutoConfig() {
        System.out.println(redisConfig.toString());
    }
}
