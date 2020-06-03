package cn.choleece.base.jdk.collection.aqs;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @author choleece
 * @Description: cyclic barrier client
 * @Date 2020-06-03 22:50
 * http://note.youdao.com/noteshare?id=23e4c42f2682f2335508b4d024fefd22
 **/
public class CyclicBarrierClient {
    public static void main1(String[] args) {

        CyclicBarrier barrier = new CyclicBarrier(3);

        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + "到达屏障...");
                    barrier.await();

                    System.out.println(Thread.currentThread().getName() + "所有的线程都已到达屏障...");
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }, "thread-" + i).start();
        }
    }

    public static void main2(String[] args) {

        CyclicBarrier barrier = new CyclicBarrier(4);

        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + "到达屏障...");
                    barrier.await();

                    System.out.println(Thread.currentThread().getName() + "所有的线程都已到达屏障...");
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }, "thread-" + i).start();
        }
    }

    public static void main(String[] args) {

        CyclicBarrier barrier = new CyclicBarrier(3, new Runnable() {
            @Override
            public void run() {
                System.out.println("所有任务都达到barrier状态...");
            }
        });

        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + "到达屏障...");
                    barrier.await();

                    System.out.println(Thread.currentThread().getName() + "所有的线程都已到达屏障...");
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }, "thread-" + i).start();
        }
    }
}
