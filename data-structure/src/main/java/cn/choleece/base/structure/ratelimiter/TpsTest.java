package cn.choleece.base.structure.ratelimiter;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-22 19:48
 **/
public class TpsTest {

    public static ThreadPoolExecutor executer = new ThreadPoolExecutor(4, 4, 4, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(500));

    public static void main(String[] args) throws Exception {
        while (true) {
            TaskDO task = new TaskDO("calcu_item_value");
            if (!SpeedControlHelper.speedControl(task.getTaskType())) {
                print("被限流..");
                continue;
            }
            executer.submit(task);
            print("添加任务成功");
            Thread.sleep(10);
        }
    }

    public static void print(Object obj) {
        System.out.print(obj.toString());
    }
}
