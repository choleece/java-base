package cn.choleece.base.jdk.concurrent.lock.countdown;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CountDownLatchTest implements Runnable {

    final static CountDownLatch latch = new CountDownLatch(11);
    final static CountDownLatchTest demo = new CountDownLatchTest();

    @Override
    public void run() {
        try {
            Thread.sleep(new Random().nextInt(10) * 1000);
            System.out.println(Thread.currentThread().getName() + " check complete");

            latch.countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executor.submit(demo);
        }

        latch.await();

        System.out.println("done!!!!");

        executor.shutdown();
    }
}
