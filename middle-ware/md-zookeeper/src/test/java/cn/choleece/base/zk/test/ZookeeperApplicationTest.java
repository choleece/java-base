package cn.choleece.base.zk.test;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2020-07-11 23:24
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class ZookeeperApplicationTest {
    @Autowired
    private CuratorFramework curator;

    @Test
    public void testCreatePersistentNode() {
        try {
            String result = curator.create().creatingParentsIfNeeded().
                    withMode(CreateMode.PERSISTENT).forPath("/choleece/zk/test1");
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSetPersistentNodeData() {
        try {
            Stat stat = curator.setData().forPath("/choleece/zk/test1", "choleece".getBytes());
            System.out.println(stat.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
