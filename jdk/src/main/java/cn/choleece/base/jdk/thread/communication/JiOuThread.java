package cn.choleece.base.jdk.thread.communication;

/**
 * @description: 从1-100， 线程1先打印技术，线程2打印偶数，从1开始
 * @author: choleece
 * @time: 2020-10-26 15:56
 */
public class JiOuThread {

    private boolean flag = true;
    private int num = 1;

    public static void main(String[] args) {

        JiOuThread jiOuThread = new JiOuThread();
        new JiThread(jiOuThread).start();
        new OuThread(jiOuThread).start();
    }

    public static class JiThread extends Thread {
        JiOuThread jiOuThread;

        public JiThread(JiOuThread jiOuThread) {
            this.jiOuThread = jiOuThread;
        }

        @Override
        public void run() {
            synchronized (jiOuThread) {
                while (jiOuThread.num <= 100) {
                    if (jiOuThread.flag) {
                        System.out.println("奇数线程: " + jiOuThread.num);
                        jiOuThread.num += 1;
                        jiOuThread.flag = false;
                        jiOuThread.notify();
                    } else {
                        try {
                            jiOuThread.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public static class OuThread extends Thread {
        JiOuThread jiOuThread;

        public OuThread(JiOuThread jiOuThread) {
            this.jiOuThread = jiOuThread;
        }

        @Override
        public void run() {
            synchronized (jiOuThread) {
                while (jiOuThread.num <= 100) {
                    if (!jiOuThread.flag) {
                        System.out.println("偶数线程: " + jiOuThread.num);
                        jiOuThread.num += 1;
                        jiOuThread.flag = true;
                        jiOuThread.notify();
                    } else {
                        try {
                            jiOuThread.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
