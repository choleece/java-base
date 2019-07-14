package cn.choleece.base.thread;

public class ThreadSleepTest2 implements Runnable {

    String myName = "choleece";

    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    @Override
    public void run() {

        synchronized (myName) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName() + " is running....");
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadSleepTest2 sleepTest = new ThreadSleepTest2();
        Thread thread1 = new Thread(sleepTest);
        thread1.setName("thread-1");
        thread1.start();
        Thread.sleep(300);
        synchronized (sleepTest.getMyName()) {
            System.out.println("main thread is running......");
        }
    }
}
