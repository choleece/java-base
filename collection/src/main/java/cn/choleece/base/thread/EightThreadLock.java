package cn.choleece.base.thread;

/**
 * @author choleece
 * @Description: 线程8锁
 * @Date 2019-10-09 21:59
 **/
public class EightThreadLock {

    public static void main(String[] args) {
        /**
         * 测试方法1
         */
//         testThread1();

        /**
         * 测试方法2
         */
//         testThread2();

        /**
         * 测试方法3
         */
//        testThread3();

        /**
         * 测试方法4
         */
//        testThread4();

        /**
         * 测试方法5
         */
//        testThread5();
    }

    /**
     * 锁在对象上
     */
    static class LockOne {
        private String mutex = "choleece";

        public synchronized String readMutex() {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(Thread.currentThread().getName() + " read mutex");
            return mutex;
        }

        public synchronized String readMutex2() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(Thread.currentThread().getName() + " read mutex2");
            return mutex;
        }
    }

    /**
     * 锁在对象上，两个线程共同竞争lockOne这个对象，打印结果如下
     * Thread-0 read mutex
     * Thread-1 read mutex2
     */
    public static void testThread1() {
        LockOne lockOne = new LockOne();


        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                lockOne.readMutex();
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                lockOne.readMutex2();
            }
        });

        thread1.start();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        thread2.start();

    }

    /**
     * 一个锁在静态方法上，一个锁在普通方法上
     */
    static class LockTwo {
        private static String mutex = "choleece";

        public synchronized String readMutex() {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(Thread.currentThread().getName() + " read mutex");
            return mutex;
        }

        public static synchronized String readMutex2() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(Thread.currentThread().getName() + " read mutex2");
            return mutex;
        }
    }

    /**
     * 一个锁在对象方法上，一个锁在类方法上，二者不同时竞争同一个内容，打印结果如下
     * Thread-1 read mutex2
     * Thread-0 read mutex
     */
    public static void testThread2() {
        LockTwo lockTwo = new LockTwo();

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                lockTwo.readMutex();
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                LockTwo.readMutex2();
            }
        });

        thread1.start();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        thread2.start();
    }

    /**
     * 一个锁在静态方法上，一个锁在普通方法上
     */
    static class LockThree {
        private static String mutex = "choleece";

        public static synchronized String readMutex() {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(Thread.currentThread().getName() + " read mutex");
            return mutex;
        }

        public static synchronized String readMutex2() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(Thread.currentThread().getName() + " read mutex2");
            return mutex;
        }
    }

    /**
     * 两个锁都在类方法上，二者同时竞争LockThree这个类，打印结果如下
     * Thread-0 read mutex
     * Thread-1 read mutex2
     */
    public static void testThread3() {

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                LockThree.readMutex();
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                LockThree.readMutex2();
            }
        });

        thread1.start();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        thread2.start();
    }

    /**
     * 普通的的方法上加锁
     */
    static class LockFour {
        private String mutex = "choleece";

        public synchronized String readMutex() {
            System.out.println(Thread.currentThread().getName() + " read mutex");
            return mutex;
        }

        public synchronized String readMutex2() {

            System.out.println(Thread.currentThread().getName() + " read mutex2");
            return mutex;
        }
    }

    /**
     * 锁在对象上，但是有多个对象，不存在多个线程竞争同一个对象的情况，所以此处无锁竞争，打印结果如下：
     * Thread-0 read mutex
     * Thread-1 read mutex2
     */
    public static void testThread4() {

        LockFour four1 = new LockFour();
        LockFour four2 = new LockFour();

        new Thread(new Runnable() {
            @Override
            public void run() {
                four1.readMutex();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                four2.readMutex2();
            }
        }).start();
    }

    /**
     * 普通的的方法上加锁，一个加锁，一个不加锁
     */
    static class LockFive {
        private String mutex = "choleece";

        public synchronized String readMutex() {

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(Thread.currentThread().getName() + " read mutex");
            return mutex;
        }

        public String readMutex2() {

            System.out.println(Thread.currentThread().getName() + " read mutex2");
            return mutex;
        }
    }

    /**
     * 锁在对象上，一个方法加锁，一个方法不加锁，不加锁的方法不参与竞争，所以不用关心锁是否被占用，打印结果如下：
     * Thread-1 read mutex2
     * Thread-0 read mutex
     */
    public static void testThread5() {

        LockFive five = new LockFive();

        new Thread(new Runnable() {
            @Override
            public void run() {
                five.readMutex();
            }
        }).start();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                five.readMutex2();
            }
        }).start();
    }
}