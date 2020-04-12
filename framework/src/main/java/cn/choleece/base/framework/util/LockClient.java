package cn.choleece.base.framework.util;

import cn.choleece.base.framework.util.lock.Lock;
import cn.choleece.base.framework.util.lock.RedLock;

/**
 * @author choleece
 * @Description: 分布式锁测试
 * @Date 2020-04-10 23:35
 **/
public class LockClient {

    public static void main(String[] args) {
        redLockTest();
    }

    /**
     * redis实现的分布式锁测试,现在设置的锁超时时间为10s，有一百个线程竞争，每个线程执行任务时间为1s，
     * 最后会有不多于10个线程获取锁成功并得到执行，另外几个失败
     */
    public static void redLockTest() {
        Lock lock = new RedLock();

        int maxThread = 100;

        for (int i = 0; i < maxThread; i++) {
            String randomId = "thread-" + i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (lock.lock(randomId)) {
                            System.out.println(Thread.currentThread().getName() + " 获得锁...");
                        } else {
                            System.out.println(Thread.currentThread().getName() + " 未获得锁...");
                        }

                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } finally {
                        lock.unlock(randomId);
                    }
                }
            }, randomId).start();
        }
    }
}
