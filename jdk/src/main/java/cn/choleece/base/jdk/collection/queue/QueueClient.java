package cn.choleece.base.jdk.collection.queue;

import java.util.concurrent.*;

/**
 * @author choleece
 * @Description: 队列：普通队列和阻塞队列
 * @Date 2020-05-10 18:50
 **/
public class QueueClient {

    public static void main(String[] args) {
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue(4);

        try {
            blockingQueue.put("444");
            blockingQueue.put("444");
            blockingQueue.put("444");
            blockingQueue.put("444");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            blockingQueue.add("444");
        } catch (Exception e) {
            System.out.println("队列满了...");
            e.printStackTrace();
        }

        try {
            String obj = blockingQueue.poll(5, TimeUnit.SECONDS);
            System.out.println(obj);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
