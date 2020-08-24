package cn.choleece.base.jdk.thread;

/**
 * @author choleece
 * @Description: 多线程测试
 * http://note.youdao.com/noteshare?id=db9760505aced233f4aba952037fc978
 * @Date 2020-05-24 11:40
 **/
public class ThreadClient {

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new MyThread();
        thread1.setName("Thread-1");

        Object o = new Object();

        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (o) {
                    try {
                        o.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println("thread2执行完了...");
                }
            }
        }, "thread2").start();

        System.out.println("主线程...");

        thread1.start();
        thread1.join();

        System.out.println("子线程执行完了。。。");
    }
}

class MyThread extends Thread {
    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            System.out.println("我已经睡了1秒了");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
