package cn.choleece.base.jdk.thread.exercise;

/**
 * @author choleece
 * @Description: 假设车库有3个车位（可以通过boolean[]数组来表示车库）可以停车，写一个程序模拟多个用户开车离开，停车入库的效果。注意：车位有车时不能停车
 * @Date 2020-08-25 22:52
 **/
public class ParkTest {

    /**
     * 锁竞争资源
     */
    static Object lock = new Object();

    /**
     * 停车位
     */
    static int state = 3;

    /**
     * 假设车库有3个车位（可以通过boolean[]数组来表示车库）可以停车，写一个程序模拟多个用户开车离开，停车入库的效果。注意：车位有车时不能停车
     * @param args
     */
    public static void main(String[] args) {

        ParkTest park = new ParkTest();

        new Thread(() -> {
            while (true) {
                park.park();
            }
        }, "user-1").start();

        new Thread(() -> {
            while (true) {
                // 模拟耗时操作
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                park.unpark();
            }
        }, "user-2").start();
    }

    /**
     * 停车
     */
    void park() {
        synchronized (lock) {
            if (state == 0) {
                try {
                    System.out.println("停车位已满...");
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                state--;
                System.out.println("车辆已停， 当前剩余车位" + state);
                lock.notifyAll();
            }
        }
    }

    /**
     * 开走
     */
    void unpark() {
        synchronized (lock) {
            if (state == 3) {
                System.out.println("停车场还未停车...");
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                state++;
                System.out.println("车辆已开出，，目前剩余车位" + state);
                lock.notifyAll();
            }
        }
    }

}
