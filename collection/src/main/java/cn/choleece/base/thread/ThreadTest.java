package cn.choleece.base.thread;

/**
 * Java线程的实现方式 有两种方式，一种是implements runnable, 一种是extends thread, 再有就是通过线程池
 */
public class ThreadTest implements Runnable {

    @Override
    public void run() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("child thread run......");
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadTest threadTest = new ThreadTest();
        Thread thread = new Thread(threadTest);
        thread.start();

        Thread.sleep(300);

        System.out.println("main thread run......");
    }
}
