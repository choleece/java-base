package cn.choleece.base.thread;

public class ThreadYieldTest implements Runnable {
    @Override
    public void run() {
//        try {
//            Thread.sleep(200);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        System.out.println("child thread run......");
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadJoinTest threadTest = new ThreadJoinTest();
        Thread thread = new Thread(threadTest);
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
        synchronized (thread) {

            thread.wait();
        }

//        Thread.yield();
        System.out.println("main thread run......");
    }
}
