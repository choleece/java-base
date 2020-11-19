package cn.choleece.base.jdk.thread.communication;

/**
 * @description: 线程通信 等待通知机制
 * @author: choleece
 * @time: 2019-10-18 17:18
 */
public class TwoThreadWaitNotify {

    public boolean flag = true;

    public int start = 0;

    public static void main(String[] args) {
        TwoThreadWaitNotify two = new TwoThreadWaitNotify();

        Thread thread1 = new Thread(new PrintJiNum(two));
        Thread thread2 = new Thread(new PrintOuNum(two));

        thread1.start();

        thread2.start();
    }

    static class PrintJiNum implements Runnable {

        private TwoThreadWaitNotify number;

        public PrintJiNum(TwoThreadWaitNotify number) {
            this.number = number;
        }

        @Override
        public void run() {
            synchronized (number) {

                while (number.start <= 100) {
                    // 如果奇数线程抢到
                    if (number.flag) {
                        System.out.println("偶数线程抢到锁: " + number.start);

                        number.start++;

                        number.flag = false;

                        number.notify();
                    } else {
                        try {
                            number.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    static class PrintOuNum implements Runnable {
        private TwoThreadWaitNotify number;

        public PrintOuNum(TwoThreadWaitNotify number) {
            this.number = number;
        }

        @Override
        public void run() {
            synchronized (number) {
                while (number.start <= 100) {
                    // 如果奇数线程抢到
                    if (!number.flag) {
                        System.out.println("奇数线程抢到锁: " + number.start);

                        number.start++;

                        number.flag = true;

                        number.notify();
                    } else {
                        try {
                            number.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
