package cn.choleece.base.leetcode.code1115;

/**
 * @description: TODO
 * @author: sf
 * @time: 2019-10-18 13:16
 */
public class PrintOdAndEven {

    public int num = 0;

    public static void main(String[] args) {

        PrintOdAndEven lock = new PrintOdAndEven();

        Thread thread1 = new Thread(new PrintEven(lock));
        Thread thread2 = new Thread(new PrintOdd(lock));

        thread1.start();
        thread2.start();

    }

    static class PrintEven implements Runnable {

        private PrintOdAndEven lock;

        // 用于与多线程共享同一个lock
        public PrintEven(PrintOdAndEven lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            synchronized (lock) {
                while (lock.num <= 100) {
                    if (lock.num % 2 == 0) {
                        System.out.println("偶数线程开始打印: " + lock.num);

                        lock.num++;

                        // 通知奇数线程可以打印了
                        lock.notify();
                    } else {
                        // 如果又到了偶数线程，那么需要将偶数线程挂起
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    static class PrintOdd implements Runnable {
        private PrintOdAndEven lock;

        public PrintOdd(PrintOdAndEven lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            synchronized (lock) {
                while (lock.num <= 100) {
                    if (lock.num % 2 == 1) {
                        System.out.println("奇数线程开始打印: " + lock.num);

                        lock.num++;

                        // 通知偶数线程可以打印了
                        lock.notify();
                    } else {
                        // 如果重复进入奇数线程，那么将奇数线程挂起
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
