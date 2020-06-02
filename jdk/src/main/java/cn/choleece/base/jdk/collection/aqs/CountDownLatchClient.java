package cn.choleece.base.jdk.collection.aqs;

import java.util.concurrent.CountDownLatch;

/**
 * @author choleece
 * @Description: CountDownLatch
 * @Date 2020-06-01 23:13
 **/
public class CountDownLatchClient {

    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(3);

        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName());
                latch.countDown();
            }, "thread-" + i).start();
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("i am the main thread... wait for the thread completed...");
    }
}
