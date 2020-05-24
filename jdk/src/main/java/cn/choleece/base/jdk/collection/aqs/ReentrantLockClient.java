package cn.choleece.base.jdk.collection.aqs;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author choleece
 * @Description: Reentrant Lock Client
 * @Date 2020-05-18 21:46
 **/
public class ReentrantLockClient {

    static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {

        Thread t1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("我是t1");
                    }
                }, "t1");

        Thread t2 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("我是t2");
                    }
                }, "t2");

        t1.start();
        t2.start();
    }

    static void testSync() {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName());
        } finally{
            lock.unlock();
        }
    }

}
