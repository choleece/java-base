package cn.choleece.base.thread;

public class ThreadSleepTest1 implements Runnable {

    @Override
    public void run() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " is running....");
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " state is " + Thread.currentThread().getState());
        ThreadSleepTest1 sleepTest = new ThreadSleepTest1();
        Thread thread1 = new Thread(sleepTest);
        thread1.setName("thread-1");
        thread1.start();
        System.out.println("thread-1's state is " + thread1.getState());
        System.out.println(Thread.currentThread().getName() + " state is " + Thread.currentThread().getState());
        Thread.sleep(300);
        System.out.println(thread1.getState());
        System.out.println(Thread.currentThread().getName() + " state is " + Thread.currentThread().getState());
        System.out.println("main thread is running......");
    }
}
