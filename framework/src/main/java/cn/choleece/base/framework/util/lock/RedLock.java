package cn.choleece.base.framework.util.lock;

import cn.choleece.base.framework.redis.RedisConfig;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.UUID;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-11-03 15:41
 *
 * 参考 https://juejin.im/post/5b737b9b518825613d3894f4
 *
 * https://www.jianshu.com/p/47fd7f86c848
 **/
@Component
public class RedLock implements Lock {

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

    private static final Long TRY_LOCK_TIMEOUT = 5L * 1000;

    /**
     * KEY 自动过期时间
     *
     * redis key 的过期策略有两种
     * 1. 后台任务随机清理
     * 2. 查询出来的时候，将过期时间做比较，如果过期了就清理掉（guava也是这个原理）
     */
    private static int TIME_OUT = 100;

    public static final String OK = "OK";

    private static final String STR_1 = "1";

    /**
     * 方法返回代表获取到锁了
     *
     * 思考点：如果任务执行时间超过锁过期的时间？（考虑开启一个任务，定时刷新锁的过期时间，保证原子行，使用lua 脚本进行刷新）
     *
     * https://blog.csdn.net/wutengfei_java/article/details/100699538
     *
     * 具体使用可以参考redisson 实现，开启一个类似于看门狗的线程，针对锁进行续期
     *
     * cluster 或者 master-slave架构的分布式锁，会存在一个问题，client1 获取锁成功，在从master复制到slave的时候，master宕机，
     * 但是此时client1以为获取到了锁，此时client2去新的master获取锁，也获得了，这个时候业务可能产生脏数据
     */
    @Override
    public Boolean lock(String randomId) {

        Long startTime = System.currentTimeMillis();

        while (true) {
            if (System.currentTimeMillis() - startTime > TRY_LOCK_TIMEOUT) {
                return false;
            }

           if (OK.equals(RedisConfig.jedis.set(LOCK_KEY, randomId, NOT_EXIST, SECONDS, TIME_OUT))) {
               return true;
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Boolean unlock(String randomId) {
        // 使用lua脚本进行原子删除操作
        String checkAndDelScript = "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                "return redis.call('del', KEYS[1]) " +
                "else " +
                "return 0 " +
                "end";

        if (STR_1.equals(RedisConfig.jedis.eval(checkAndDelScript,
                Collections.singletonList(LOCK_KEY), Collections.singletonList(randomId)))) {
            return true;
        }
        return false;
    }
}
