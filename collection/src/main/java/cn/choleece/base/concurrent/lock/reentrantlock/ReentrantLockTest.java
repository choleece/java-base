package cn.choleece.base.concurrent.lock.reentrantlock;

/**
 * @author choleece
 * @Description: reentrant lock test
 * @Date 2019-09-16 22:12
 **/
public class ReentrantLockTest {

    private String criticalResource;

    public synchronized String getCriticalResource() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return criticalResource;
    }

    public void setCriticalResource(String criticalResource) {
        this.criticalResource = criticalResource;
    }

    public static void main(String[] args) {

        ReentrantLockTest test = new ReentrantLockTest();
        test.setCriticalResource("hello choleece");

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {

                System.out.println("thread 1 gets critical resource " + test.getCriticalResource());
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("thread 2 gets critical resource " + test.getCriticalResource());
            }
        });

        thread1.start();
        thread2.start();

        System.out.println("main thread gets critical resource " + test.getCriticalResource());


    }

}
