package cn.choleece.base.framework;

import cn.choleece.base.framework.config.BeanTest;
import cn.choleece.base.framework.redis.core.RedisCallback;
import cn.choleece.base.framework.template.CusRedisTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.JedisCommands;

import java.util.UUID;

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

        boolean result = redisTemplate.opsForValue().setIfAbsent("name", "chaoli");
        System.out.println(result);

        System.out.println(redisTemplate.opsForValue().get("name"));

        RedisCallback<String> callback = (connection) -> {
            JedisCommands commands = (JedisCommands) connection.getNativeConnection();
            String uuid = UUID.randomUUID().toString();
            return commands.set("name1", uuid, "NX", "EX", 100);
        };
        Object result1 = redisTemplate.execute(callback);

        System.out.println(result1);
    }
}
