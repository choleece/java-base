package cn.choleece.base.leetcode.code1115;

/**
 * @description: TODO
 * @author: sf
 * @time: 2019-10-18 13:27
 */
public class Solution1115 {
    private Object lock = new Object();
    private int n;

    private volatile int count = 1;

    public Solution1115(int n) {
        this.n = n;
    }

    public void foo(Runnable printFoo) throws InterruptedException {

        for (int i = 0; i < n; i++) {
            synchronized (lock) {
                while (count % 2 == 0) {
                    lock.wait();
                }

                count++;
                // printFoo.run() outputs "foo". Do not change or remove this line.
                printFoo.run();
                lock.notify();
            }
        }

    }

    public void bar(Runnable printBar) throws InterruptedException {

        for (int i = 0; i < n; i++) {
            synchronized (lock) {
                while (count % 2 == 1) {
                    lock.wait();
                }
                count++;
                // printBar.run() outputs "bar". Do not change or remove this line.
                printBar.run();

                lock.notify();
            }
        }
    }

    static class PrintFoo implements Runnable {
        @Override
        public void run() {
            System.out.print("foo");
        }
    }

    static class PrintBar implements Runnable {
        @Override
        public void run() {
            System.out.print("bar");
        }
    }

    public static void main(String[] args) {
        Solution1115 fooBar = new Solution1115(1);

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    fooBar.foo(new PrintFoo());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    fooBar.bar(new PrintBar());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread1.start();

        // 保证thread1 优先执行
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        thread2.start();
    }
}