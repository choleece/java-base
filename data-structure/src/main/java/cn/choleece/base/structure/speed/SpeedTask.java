package cn.choleece.base.structure.speed;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-22 20:23
 **/
public class SpeedTask implements Runnable {

    private String taskName;

    public SpeedTask(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskName() {
        return this.taskName;
    }

    @Override
    public void run() {
        try {
            long startTime = System.currentTimeMillis();
            Thread.sleep(10);
            long endTime = System.currentTimeMillis();

            // 去设置限流统计
            SpeedHelper.setTask(this.taskName, endTime - startTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
