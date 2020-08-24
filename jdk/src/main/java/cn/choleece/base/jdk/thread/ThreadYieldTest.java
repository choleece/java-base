package cn.choleece.base.jdk.thread;

/**
 * @author choleece
 * @Description: Tield()的调用是对线程调度器的一种"建议"，使当前线程由执行状态，变成为就绪状态，让出cpu时间，在下一个线程执行时候，此线程有可能被执行，也有可能没有被执行
 * @Date 2019-10-09 23:22
 * 参考 https://www.jianshu.com/p/dd204c853b43
 **/
public class ThreadYieldTest extends Thread {

    public ThreadYieldTest(String name) {
        super(name);
    }

    @Override
    public void run() {
        for (int i = 0; i < 50; i++) {

            System.out.println(getName() + " --- " + i);

            // 当执行yield时，让出CPU执行权，下一轮执行，有可能继续分配到当前线程，也有可能分配到其他线程
            // 当i为30时，该线程就会把CPU时间让掉，让其他或者自己的线程执行（也就是谁先抢到谁执行）
            // 执行yield不对锁资源进行释放
            if (i == 30) {
                yield();
            }
        }
    }

    public static void main(String[] args) {
        new ThreadYieldTest("thread-1").start();

        new ThreadYieldTest("thread-2").start();
    }
}
