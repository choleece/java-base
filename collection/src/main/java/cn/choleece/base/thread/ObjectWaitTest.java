package cn.choleece.base.thread;

public class ObjectWaitTest {

    static Object obj = new Object();

    public static class Thread1 extends Thread {

        public Thread1(String name) {
            super(name);
        }

        @Override
        public void run() {
            synchronized (obj) {
                try {
                    System.out.println(getName() + " is coming...");
                    obj.wait();
                    System.out.println(getName() + " is waiting...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(getName() + " is ending......");
            }
        }
    }

    public static class Thread2 extends Thread {

        public Thread2(String name) {
            super(name);
        }

        @Override
        public void run() {
            synchronized (obj) {
                System.out.println(getName() + " is coming...");
                obj.notify();
                System.out.println(getName() + " is waiting...");
                System.out.println(getName() + " is ending......");
            }
        }
    }

    public static void main(String[] args) {
        Thread thread1 = new Thread1("thread1");
        Thread thread12 = new Thread1("thread12");
        Thread thread2 = new Thread2("thread2");

        thread1.start();
        thread12.start();
//        thread2.start();
    }
}
