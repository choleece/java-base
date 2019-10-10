package cn.choleece.base.thread;

import java.util.LinkedList;
import java.util.List;

/**
 * @author choleece
 * @Description: 线程间通信
 * @Date 2019-10-10 23:09
 **/
public class ThreadCommunication {

    public static void main(String[] args) {
        testCommunicationOne();
    }

    /**
     * 利用一个线程死循环去轮询，这样做会比较浪费资源，相当于开了一线程，一直在跑任务，直到满足退出条件，才抛出中断异常，从而结束任务
     */
    public static void testCommunicationOne() {

        List<String> list = new LinkedList<>();

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    list.add("choleece");

                    try {
                        /**
                         * sleep期间，不会释放线程锁占有的锁资源，跟yield类似
                         */
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    while (true) {
                        if (list.size() == 2) {
                            throw new InterruptedException();
                        }
                        System.out.println(Thread.currentThread().getName() + " list size: " + list.size());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread1.start();

        thread2.start();

        System.out.println(Thread.currentThread().getName() + " list size: " + list.size());
    }
}
