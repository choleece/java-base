package cn.choleece.base.md.redis.distribute.limit;

import cn.choleece.base.md.redis.util.ScriptUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collections;

/**
 * @author choleece
 * @Description: redis distribute limit
 * @Date 2020-09-06 13:38
 **/
@Component
public class RedisLimit {
    @Resource
    private RedisTemplate redisTemplate;

    private DefaultRedisScript<Long> unlockScript;

    private static final Long FAIL_CODE = 0L;

    private int limit = 200;

    /**
     * limit traffic
     * @return if true
     */
    public boolean limit() {
        return !FAIL_CODE.equals((Long) limitRequest());
    }

    private Object limitRequest() {
        String key = String.valueOf(System.currentTimeMillis() / 1000);
        return redisTemplate.execute(unlockScript, Collections.singletonList(key), String.valueOf(limit));
    }

    /**
     * read lua script
     */
    @PostConstruct
    private void buildScript() {
        unlockScript = new DefaultRedisScript<Long>(ScriptUtils.getScript("script/limit.lua"), Long.class);
    }

}
