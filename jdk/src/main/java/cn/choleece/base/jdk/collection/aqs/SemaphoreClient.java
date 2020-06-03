package cn.choleece.base.jdk.collection.aqs;

import java.util.concurrent.Semaphore;

/**
 * @author choleece
 * @Description: Semaphore client
 * http://note.youdao.com/noteshare?id=94181d42e2226ea71dc05b3383451533
 * @Date 2020-06-03 22:18
 **/
public class SemaphoreClient {

    public static void main1(String[] args) {
        Semaphore semaphore = new Semaphore(5);

        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                try {
                    semaphore.acquire();
                    System.out.println("获取...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    // semaphore.release();
                }
            }, "thread-" + i).start();
        }

        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("此示例会不会执行结束..");
    }

    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(5);

        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                try {
                    semaphore.acquire();
                    System.out.println("获取...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                     semaphore.release();
                }
            }, "thread-" + i).start();
        }

        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("此示例会执行结束..");
    }

}
