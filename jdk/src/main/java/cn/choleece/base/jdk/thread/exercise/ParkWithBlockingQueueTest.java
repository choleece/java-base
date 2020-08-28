package cn.choleece.base.jdk.thread.exercise;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author choleece
 * @Description: 假设车库有3个车位（可以通过boolean[]数组来表示车库）可以停车，写一个程序模拟多个用户开车离开，停车入库的效果。注意：车位有车时不能停车
 * @Date 2020-08-26 22:29
 **/
public class ParkWithBlockingQueueTest {

    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(3);

        Producer p1 = new Producer(queue);
        p1.start();

        Consumer c1 = new Consumer(queue);
        c1.start();
        Consumer c2 = new Consumer(queue);
        c2.start();
    }
}

class Producer extends Thread {

    BlockingQueue queue;

    public Producer(BlockingQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(1000);
                queue.put(1);
                System.out.println("已停了一个车辆,停车场剩余车位" + (3 - queue.size()));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Consumer extends Thread {
    BlockingQueue queue;

    public Consumer(BlockingQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(2000);
                queue.take();
                System.out.println("已开出一两车，停车场剩余车位" + (3 - queue.size()));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
