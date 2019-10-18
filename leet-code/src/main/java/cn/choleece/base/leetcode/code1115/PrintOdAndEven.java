package cn.choleece.base.leetcode.code1115;

/**
 * @description: TODO
 * @author: sf
 * @time: 2019-10-18 13:16
 */
public class PrintOdAndEven {

    public static void main(String[] args) {
        Object lock1 = new Object();
        Object lock2 = new Object();

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock1) {
                    for (int i = 0; i <= 100; i++) {
                        if (i % 2 == 0) {
                            System.out.println("线程1打印偶数: " + i);

                            try {
                                // 打印完后，线程释放锁资源
                                lock1.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            lock1.notify();
                        }
                    }
                }
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock1) {
                    for (int i = 0; i <= 100; i++) {
                        if (i % 2 != 0) {
                            System.out.println("线程2打印奇数:" + i);
                            try {
                                lock1.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            lock1.notify();
                        }
                    }
                }
            }
        });

        thread1.start();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        thread2.start();
    }
}
