package cn.choleece.base.md.redis.controller;

import cn.choleece.base.md.redis.distribute.annotation.SpringControllerLimit;
import cn.choleece.base.md.redis.service.RedisService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author choleece
 * @Description: redis controller
 * @Date 2020-09-06 11:01
 **/
@RestController
@RequestMapping("/redis")
public class RedisController {
    @Resource
    private RedisService redisService;

    @GetMapping("/template")
    @SpringControllerLimit
    public String template() {
        redisService.templateGet();
        return "ok";
    }
}
