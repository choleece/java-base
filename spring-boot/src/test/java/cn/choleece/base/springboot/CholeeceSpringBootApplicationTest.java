package cn.choleece.base.springboot;

import cn.choleece.base.springboot.autoconfig.redis.properties.RedisConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
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
    private RedisTemplate redisTemplate;

    @Test
    public void testAutoConfig() {
//        redisTemplate.opsForList().leftPush("languages", "Java");
        String lang = (String) redisTemplate.opsForList().leftPop("languages");
        System.out.println(lang);
    }
}
