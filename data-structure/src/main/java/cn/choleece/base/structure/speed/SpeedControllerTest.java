package cn.choleece.base.structure.speed;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-22 20:10
 **/
public class SpeedControllerTest {

    static ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 20, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(20));

    public static void main(String[] args) {
        while (true) {

            SpeedTask speedTask = new SpeedTask("task1");

            // 执行任务
            if (SpeedHelper.needSpeedControl(speedTask.getTaskName())) {

                System.out.println("需要限流");

                continue;
            }

            executor.execute(speedTask);

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
