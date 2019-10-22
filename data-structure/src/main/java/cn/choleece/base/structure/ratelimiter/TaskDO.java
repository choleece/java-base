package cn.choleece.base.structure.ratelimiter;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-22 19:45
 **/
public class TaskDO implements Runnable {
    private String taskType;

    public TaskDO(String taskType) {
        this.taskType = taskType;
    }

    public String getTaskType() {
        return taskType;
    }

    @Override
    public void run() {
        try {
            long startTime = System.currentTimeMillis();
            Thread.sleep(15);
            long endTime = System.currentTimeMillis();

            // 设置任务执行的时间
            SpeedControlHelper.setExecTime(taskType, endTime - startTime);
        } catch (InterruptedException e) {
            // logger..
        }
    }
}
