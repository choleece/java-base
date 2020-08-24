package cn.choleece.base.jdk.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;

/**
 * @author choleece
 * @Description: 多线程框架 executor
 * @Date 2019-10-09 23:33
 **/
public class ExecutorTest {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService service1 = Executors.newCachedThreadPool();

        ExecutorService service2 = Executors.newFixedThreadPool(2);

        ExecutorService service3 = Executors.newSingleThreadExecutor();

        ExecutorService service4 = Executors.newScheduledThreadPool(1);

        BlockingQueue<Integer> queue = new SynchronousQueue<>();
//        System. out .print(queue.offer(1) + " ");
//        System. out .print(queue.offer(2) + " ");
//        System. out .print(queue.offer(3) + " ");
        System. out .print(queue.take() + " ");
        System. out .println(queue.size());
    }

}
