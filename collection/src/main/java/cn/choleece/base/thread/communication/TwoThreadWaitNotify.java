package cn.choleece.base.thread.communication;

/**
 * @description: 线程通信 等待通知机制
 * @author: sf
 * @time: 2019-10-18 17:18
 */
public class TwoThreadWaitNotify {

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
            for (int i = 0; i <= 100; i++) {
                synchronized (number) {
                    if (i % 2 == 1) {
                        System.out.println("线程1打印奇数: " + i);
                        try {
                            number.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    number.notifyAll();
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
            for (int i = 0; i <= 100; i++) {
                synchronized (number) {
                    if (i % 2 == 0) {
                        System.out.println("线程2打印偶数: " + i);
                        try {
                            number.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    number.notifyAll();
                }
            }
        }
    }
}
