package cn.choleece.base.framework.util.lock;

import cn.choleece.base.framework.redis.core.RedisCallback;
import cn.choleece.base.framework.template.CusRedisTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCommands;

import java.util.UUID;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-11-03 15:41
 *
 * 参考 https://juejin.im/post/5b737b9b518825613d3894f4
 **/
@Component
public class RedLock implements Lock {
    @Autowired
    private CusRedisTemplate cusRedisTemplate;

    /**
     * redis key
     */
    private static final String LOCK_KEY = "lock";

    /**
     * redis value ，避免超时删掉错误的锁
     */
    private static final String LOCK_VALUE = UUID.randomUUID().toString();

    private static final String NOT_EXIST = "NX";

    /**
     * EX 代表单位为秒，PX代表单位为毫秒
     */
    private static final String SECONDS = "EX";

    /**
     * KEY 自动过期时间
     *
     * redis key 的过期策略有两种
     * 1. 后台任务随机清理
     * 2. 查询出来的时候，将过期时间做比较，如果过期了就清理掉（guava也是这个原理）
     */
    private static int TIME_OUT = 100;

    public static final String OK = "OK";

    /**
     * 方法返回代表获取到锁了
     *
     * 思考点：如果任务执行时间超过锁过期的时间？（考虑开启一个任务，定时刷新锁的过期时间，保证原子行，使用lua 脚本进行刷新）
     */
    @Override
    public void lock() {
        while (true) {
            RedisCallback<String> callback = (connection) -> {
                JedisCommands commands = (JedisCommands) connection.getNativeConnection();
                return commands.set(LOCK_KEY,LOCK_VALUE, NOT_EXIST, SECONDS, 100);
            };
            Object result = cusRedisTemplate.execute(callback);

            if (OK.equals(result)) {
                return;
            }
        }
    }

    @Override
    public void unlock() {
        // 使用lua脚本进行原子删除操作
        String checkAndDelScript = "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                "return redis.call('del', KEYS[1]) " +
                "else " +
                "return 0 " +
                "end";


    }
}
