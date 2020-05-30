package cn.choleece.base.jdk.collection.thread;

/**
 * @author choleece
 * @Description: 线程中断
 * http://note.youdao.com/noteshare?id=2852da2cc6eda3876820b8b38be69b5d
 * @Date 2020-05-30 12:07
 **/
public class ThreadInterruptedClient {

    public static void main1(String[] args) {
        Thread thread = new Thread(() -> {
           while (true) {
               Thread.yield();
           }
        });

        thread.start();
        System.out.println("主线程");
        thread.interrupt();
        System.out.println(thread.isInterrupted());
        thread.interrupt();
    }

    public static void main2(String[] args) {
        Thread thread = new Thread(() -> {
            while (true) {
                Thread.yield();
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("我被中断了，");
                    return;
                }
            }
        });

        thread.start();
        System.out.println("主线程");
        thread.interrupt();
        System.out.println(thread.isInterrupted());
        thread.interrupt();
    }

    public static void main3(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (true) {
                Thread.yield();
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("我被中断了，");
                    return;
                }

                try {
                    Thread.sleep(3000);
                    System.out.println("睡眠结束...");
                } catch (InterruptedException e) {
                    System.out.println("我的睡眠被打断了...");
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        System.out.println("主线程");
        Thread.sleep(2000);
        thread.interrupt();
        System.out.println(thread.isInterrupted());
    }

    public static void main4(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (true) {
                Thread.yield();
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("我被中断了，");
                    return;
                }

                try {
                    Thread.sleep(3000);
                    System.out.println("睡眠结束...");
                } catch (InterruptedException e) {
                    System.out.println("我的睡眠被打断了...");
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        });

        thread.start();
        System.out.println("主线程");
        Thread.sleep(2000);
        thread.interrupt();
        System.out.println(thread.isInterrupted());
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (true) {
                Thread.yield();
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("我被中断了，");
                    return;
                }

                try {
                    Thread.sleep(3000);
                    System.out.println("睡眠结束...");
                } catch (InterruptedException e) {
                    System.out.println("我的睡眠被打断了...");
                    e.printStackTrace();
                    Thread.currentThread().interrupt();

                    // 清除中断标记
                    Thread.interrupted();
                }
            }
        });

        thread.start();
        System.out.println("主线程");
        Thread.sleep(2000);
        thread.interrupt();
        System.out.println(thread.isInterrupted());
    }

}
