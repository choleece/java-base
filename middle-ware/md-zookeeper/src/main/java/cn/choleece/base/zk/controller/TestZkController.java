package cn.choleece.base.zk.controller;

import cn.choleece.base.zk.config.ZkCuratorProperty;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-07-11 15:58
 **/
@RestController
@RequestMapping("/zk")
public class TestZkController {
    @Autowired
    private ZkCuratorProperty property;
    @Autowired
    private CuratorFramework curator;

    @GetMapping("/init")
    public void testInit() {
        System.out.println(property.toString());
    }

    @GetMapping("/path")
    public void getPath(String path) {
        try {
            byte[] data = curator.getData().forPath(path);
            System.out.println(new String(data, "utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/addWatch")
    public void addWatch(String path) {
        /**
         * 这里的监听是一次性的，zookeeper提供的监听，同样也是一次性的，每当监听触发完成后，监听就会断开
         */
        Watcher watcher = watchedEvent -> System.out.println(watchedEvent.toString());

        try {
            curator.getData().usingWatcher(watcher).forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建临时节点
     * @param path
     */
    @GetMapping("/createEphemeral")
    public void createEphemeralNode(String path) {
        try {
            curator.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, "ephemeral node".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建临时有序节点
     * @param path
     */
    @GetMapping("/createEphemeralSequential")
    public void createEphemeralSequentialNode(String path) {
        try {
            curator.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(path, "ephemeral node sequential".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建持久节点
     * @param path
     */
    @GetMapping("/createPersistent")
    public void createPersistenceNode(String path) {
        try {
            curator.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path, "persistent node".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建持久有序的节点
     * @param path
     */
    @GetMapping("/createPersistentSequential")
    public void createPersistenceSequentialNode(String path) {
        try {
            curator.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath(path, "persistent node sequential".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
