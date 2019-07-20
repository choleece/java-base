package cn.choleece.base.thread;

/**
 * join方法使调用该方法的线程再次之前执行完毕，在b线程里执行a.join()，那么b会等待a执行完成后再执行b的逻辑
 */
public class ThreadJoinTest implements Runnable {

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
        ThreadJoinTest threadTest = new ThreadJoinTest();
        Thread thread = new Thread(threadTest);
        thread.start();

        thread.join();

        System.out.println("main thread run......");
    }
}
