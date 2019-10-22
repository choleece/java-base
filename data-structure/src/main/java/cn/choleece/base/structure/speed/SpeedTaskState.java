package cn.choleece.base.structure.speed;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-22 20:14
 **/
public class SpeedTaskState {

    /**
     * 任务名
     */
    private String task;

    /**
     * 该类型任务执行总时间
     */
    private int totalTime;

    /**
     * 该类型任务执行平均时间
     */
    private int avgTime;

    /**
     * 该类型任务执行次数
     */
    private int exeCount;

    private AtomicInteger exeCountPerSecond = new AtomicInteger(0);

    private long currentSecond = 0;

    public SpeedTaskState(String taskName) {
        this.task = taskName;

        reset();
    }

    void reset() {
        exeCount = 1;
        totalTime = 100;
    }

    public int calculateTaskTps() {
        if (currentSecond == System.currentTimeMillis() % 1000) {
            return exeCountPerSecond.getAndIncrement();
        } else {
            currentSecond = System.currentTimeMillis() % 1000;
            exeCountPerSecond.set(1);
            return 1;
        }
    }

    public void setTime(Long exeTime) {
        try {
            totalTime += exeTime;
            exeCount += 1;
            avgTime = totalTime / exeCount;
        } catch (Exception e) {
            reset();
        }
    }
}
