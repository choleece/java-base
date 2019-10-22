package cn.choleece.base.structure.ratelimiter;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author choleece
 * @Description: 没一个任务的访问情况
 * @Date 2019-10-22 19:46
 **/
public class TaskStateDO {

    /**
     * 任务类型
     */
    private String taskType;

    /**
     * 一个统计时间区间内的总执行次数
     */
    private int execCount;

    /**
     * 一个统计时间区间内的总执行耗时
     */
    private long totalTime;

    /**
     * 一个统计时间区间内的平均执行耗时
     */
    private int averageTime;

    /**
     * 当前1秒内执行的次数
     */
    private AtomicInteger secondExecCount = new AtomicInteger(0);

    /**
     * 当前秒
     */
    private long currentSecond = 0;

    public TaskStateDO() {
        reset();
    }

    /**
     * 获取taskType
     *
     * @return taskType
     */
    public String getTaskType() {
        return taskType;
    }

    /**
     * 设置taskType
     *
     * @param taskType 要设置的taskType
     */
    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    /**
     * 获取totalTime
     *
     * @return totalTime
     */
    public long getTotalTime() {
        return totalTime;
    }

    /**
     * 获取execCount
     *
     * @return execCount
     */
    public int getExecCount() {
        return execCount;
    }

    /**
     * 获取averageTime
     *
     * @return averageTime
     */
    public int getAverageTime() {
        return averageTime;
    }

    /**
     * 统计任务的秒级执行次数
     *
     * @return
     */
    public int calcuTps() {
        if (currentSecond == System.currentTimeMillis() / 1000) {
            return secondExecCount.incrementAndGet();// 1s的执行次数
        } else {
            currentSecond = System.currentTimeMillis() / 1000;
            secondExecCount.set(1);
            return 1;
        }
    }

    /**
     * 统计任务执行时间
     *
     * @param time
     */
    public void state(long time) {
        try {
            execCount++;
            totalTime += time;
            averageTime = (int) (totalTime / execCount);
        } catch (Exception e) {
            reset();
        }
    }

    /**
     * 重置方法，没有设置为零，防止除法抛异常
     */
    public void reset() {
        execCount = 1;
        totalTime = 100;
    }
}
