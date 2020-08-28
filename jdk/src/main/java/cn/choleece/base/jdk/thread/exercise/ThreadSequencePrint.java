package cn.choleece.base.jdk.thread.exercise;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author choleece
 * @Description: 线程顺序打印-有A,B,C三个线程, A线程输出A, B线程输出B, C线程输出C，要求, 同时启动三个线程, 按顺序输出ABC, 循环10次
 * @Date 2020-08-25 23:10
 **/
public class ThreadSequencePrint {

    private static int state = 0;

    public static void main(String[] args) {

        Lock lock = new ReentrantLock();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10;) {
                lock.lock();
                if (state % 3 == 0) {
                    System.out.println("A");
                    state++;
                    i++;
                }
                lock.unlock();
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10;) {
                lock.lock();
                if (state % 3 == 1) {
                    System.out.println("B");
                    i++;
                    state++;
                }
                lock.unlock();
            }
        }, "t1");

        Thread t3 = new Thread(() -> {
            for (int i = 0; i < 10;) {
                lock.lock();
                if (state % 3 == 2) {
                    System.out.println("C");
                    i++;
                    state++;
                }
                lock.unlock();
            }
        }, "t3");

        t1.start();
        t2.start();
        t3.start();
    }

}
