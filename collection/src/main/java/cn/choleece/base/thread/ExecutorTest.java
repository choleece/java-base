package cn.choleece.base.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author choleece
 * @Description: 多线程框架 executor
 * @Date 2019-10-09 23:33
 **/
public class ExecutorTest {

    public static void main(String[] args) {
        ExecutorService service1 = Executors.newCachedThreadPool();

        ExecutorService service2 = Executors.newFixedThreadPool(2);

        ExecutorService service3 = Executors.newSingleThreadExecutor();

        ExecutorService service4 = Executors.newScheduledThreadPool(1);
    }

}
