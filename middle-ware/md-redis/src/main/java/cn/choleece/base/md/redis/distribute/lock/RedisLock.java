package cn.choleece.base.md.redis.distribute.lock;

import cn.choleece.base.md.redis.util.ScriptUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.Duration;
import java.util.Collections;

/**
 * @author choleece
 * @Description: redis distribute lock
 * @Date 2020-09-06 13:54
 **/
@Component
public class RedisLock {

    private static Logger logger = LoggerFactory.getLogger(RedisLock.class);

    @Resource
    private RedisTemplate redisTemplate;

    private static final Long UN_LOCK_SUC_CODE = 1L;

    private static final Duration EXPIRE_LOCK_DURATION = Duration.ofSeconds(10);

    private final String lockPrefix = "dis:lock:";

    /**
     * sleep time when try lock failed
     */
    private final int sleepTime = 10;

    /**
     * time millisecond
     */
    private static final int TIME = 1000;

    /**
     * lua script
     */
    private DefaultRedisScript<Long> script;

    /**
     * Non-blocking lock, default timeout :10s
     *
     * @param key     lock business type
     * @param request value
     * @return true lock success
     * false lock fail
     */
    public boolean tryLock(String key, String request) {
        return tryLock(key, request,10 * TIME);
    }

    /**
     * blocking lock
     *
     * @param key
     * @param request
     */
    public void lock(String key, String request) throws InterruptedException {
        for (; ;) {
            if (redisTemplate.opsForValue().setIfPresent(lockPrefix + key, request, EXPIRE_LOCK_DURATION)) {
                break;
            }
            Thread.sleep(sleepTime);
        }
    }

    /**
     * blocking lock,custom time
     *
     * @param key
     * @param request
     * @param blockTime custom time
     * @return
     * @throws InterruptedException
     */
    public boolean lock(String key, String request, int blockTime) throws InterruptedException {
        while (blockTime >= 0) {
            if (redisTemplate.opsForValue().setIfPresent(lockPrefix + key, request, EXPIRE_LOCK_DURATION)) {
                return true;
            }

            blockTime -= sleepTime;
            Thread.sleep(sleepTime);
        }
        return false;
    }


    /**
     * Non-blocking lock
     *
     * @param key        lock business type
     * @param request    value
     * @param expireTime custom expireTime
     * @return true lock success
     * false lock fail
     */
    public boolean tryLock(String key, String request, int expireTime) {

        return redisTemplate.opsForValue().setIfPresent(lockPrefix + key, request, Duration.ofSeconds(expireTime));
    }


    /**
     * unlock
     *
     * @param key
     * @param request request must be the same as lock request
     * @return
     */
    public boolean unlock(String key, String request) {
        Long result = (Long) redisTemplate.execute(script, Collections.singletonList(lockPrefix + key), request);

        return UN_LOCK_SUC_CODE.equals(request);
    }


    /**
     * read lua script
     */
    @PostConstruct
    private void buildScript() {
        script = new DefaultRedisScript<Long>(ScriptUtils.getScript("script/lock.lua"), Long.class);
    }
}
