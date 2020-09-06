package cn.choleece.base.md.redis.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author choleece
 * @Description: redis service
 * @Date 2020-09-06 11:02
 **/
@Service
public class RedisService {
    @Resource
    private RedisTemplate redisTemplate;

    public void templateGet() {
        redisTemplate.opsForValue().set("sex", "M");
        redisTemplate.opsForValue().set("age", "18");
        System.out.println(redisTemplate.hasKey("stock:1:count"));
        System.out.println(redisTemplate.hasKey("stock:1:sale"));
        System.out.println(redisTemplate.hasKey("stock:1:version"));
        System.out.println(redisTemplate.hasKey("name"));
        System.out.println(redisTemplate.opsForValue().get("name"));
        System.out.println(redisTemplate.opsForValue().get("stock:1:count"));
    }
}
