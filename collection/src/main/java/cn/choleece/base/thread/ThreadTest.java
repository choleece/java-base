package cn.choleece.base.thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadTest {

    public static void main(String[] args) {
        ThreadDemo threadDemo = new ThreadDemo();

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("thread-1" + threadDemo.getA());
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("thread-2" + threadDemo.getA());
            }
        });

        thread1.start();
        thread2.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

class ThreadDemo {

    public int a = 0;

    Object syncObj = new Object();

    public int getA() {
        Lock lock = new ReentrantLock();
        lock.lock();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return a;
    }

    public int getBWithOutLock() {
        return a;
    }
}
