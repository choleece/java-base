package cn.choleece.base.framework.util.lock;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author choleece
 * @Description: zk 实现分布式锁
 * @Date 2019-11-01 23:07
 **/
//@Component
public class ZkLock implements Lock {
    @Autowired
    private ZkClient zkClient;

    private static final Logger logger = LogManager.getLogger(ZkLock.class);

    /**
     * 锁节点
     */
    private static final String LOCK_PATH = "/LOCK";

    private CountDownLatch cdl;

    /**
     * 当前请求节点的前一个节点
     */
    private String beforePath;

    /**
     * 当前请求的节点
     */
    private String currentPath;

    @PostConstruct
    public void init() {
        // 判断是否有锁节点，没有的话就创建
        if (!zkClient.exists(LOCK_PATH)) {
            zkClient.createPersistent(LOCK_PATH);

            logger.info("创建lock节点成功....");
        }
    }

    @Override
    public void lock() {
        if (!tryLock()) {
            // 对最小节点进行监听
            waitForLock();

            lock();
        } else {
            logger.info(Thread.currentThread().getName() + " 获得分布式锁！");
        }
    }

    public boolean tryLock() {

        // 如果currentPath为空，表明是第一次尝试加锁，第一次加锁赋值给currentPath
        if (currentPath == null || currentPath.length() <= 0) {

            // 创建一个临时的顺序节点
            currentPath = zkClient.createEphemeralSequential(LOCK_PATH + "/", "lock");

            logger.info("创建临时节点成功, currentPath: " + currentPath);
        }

        // 获取所有的节点并排序， 临时节点名称为自增长的字符串如：0000000400
        List<String> childrenPaths = zkClient.getChildren(LOCK_PATH);

        // 由小到大排序所有的子节点
        Collections.sort(childrenPaths);

        // 判断创建的子节点/LOCK/Node-n是否最小，即currentPath, 如果当前节点等于childrenPaths中的最小的一个就占用锁
        if ((LOCK_PATH + "/" + childrenPaths.get(0)).equals(currentPath)) {
            return true;
        }

        // 找出比创建的临时顺序节子节点/LOCK/Node-n次小的节点,并赋值给beforePath
        int wz = Collections.binarySearch(childrenPaths, currentPath.substring(6));
        beforePath = LOCK_PATH + "/" + childrenPaths.get(wz - 1);

        return false;
    }

    private void waitForLock() {
        IZkDataListener listener = new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) throws Exception {

            }

            @Override
            public void handleDataDeleted(String s) throws Exception {
                logger.info(Thread.currentThread().getName() + ": 捕获到DataDelete事件！---------------------------");

                if (cdl != null) {
                    cdl.countDown();
                }
            }
        };

        // 对次小节点进行监听,即beforePath-给排在前面的的节点增加数据删除的watcher
        zkClient.subscribeDataChanges(beforePath, listener);

        if (zkClient.exists(beforePath)) {
            cdl = new CountDownLatch(1);

            try {
                cdl.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        zkClient.unsubscribeDataChanges(beforePath, listener);
    }

    @Override
    public void unlock() {
        // 释放锁，删除节点
        zkClient.delete(currentPath);
    }
}
