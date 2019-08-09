package cn.choleece.base.thread;

public class ThreadTest {

    public static void main(String[] args) {
        ThreadDemo threadDemo = new ThreadDemo();

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("锁在类上" + ThreadDemo.getA());
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("锁在对象上" + ThreadDemo.getBWithOutLock());
            }
        });

        thread1.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread2.start();
    }

}

class ThreadDemo {

    public static int a = 0;

    public synchronized static int getA() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return a;
    }

    public synchronized static int getBWithOutLock() {
        return a;
    }
}
