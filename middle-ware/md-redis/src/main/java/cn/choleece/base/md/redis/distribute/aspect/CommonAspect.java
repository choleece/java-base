package cn.choleece.base.md.redis.distribute.aspect;

import cn.choleece.base.md.redis.distribute.limit.RedisLimit;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-09-06 13:31
 **/
@Aspect
@Component
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class CommonAspect {
    private static Logger logger = LoggerFactory.getLogger(CommonAspect.class);

    @Resource
    private RedisLimit redisLimit ;

    @Pointcut("@annotation(cn.choleece.base.md.redis.distribute.annotation.CommonLimit)")
    private void check(){}

    @Before("check()")
    public void before(JoinPoint joinPoint) throws Exception {

        if (redisLimit == null) {
            throw new NullPointerException("redisLimit is null");
        }

        boolean limit = redisLimit.limit();
        if (!limit) {
            logger.warn("request has bean limited");
            throw new RuntimeException("request has bean limited") ;
        }
    }
}
