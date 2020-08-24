package cn.choleece.base.jdk.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author choleece
 * @Description: 线程池相关知识
 * @Date 2020-05-30 23:54
 **/
public class ThreadPoolClient {
    public static void main(String[] args) {
        ThreadPoolExecutor executors = new ThreadPoolExecutor(1, 2, 1000L, TimeUnit.MICROSECONDS,
                new ArrayBlockingQueue<Runnable>(2), new ThreadPoolExecutor.AbortPolicy());

        for (int i = 0; i < 1; i++) {
            executors.execute(() -> {
                System.out.println(Thread.currentThread().getName());
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        executors.allowCoreThreadTimeOut(true);

        Thread thread = new Thread(() -> {
            System.out.println("test ...");
        });

        thread.run();
        thread.start();

        try {
            Thread.sleep(3000L);
            System.out.println("结束睡眠...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(executors.getActiveCount());

        System.out.println("---");
    }
}
