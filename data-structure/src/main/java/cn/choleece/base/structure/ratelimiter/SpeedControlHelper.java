package cn.choleece.base.structure.ratelimiter;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-22 19:42
 **/
public class SpeedControlHelper {

    public static boolean speedControl(String taskType) {

        int currentTps = currentTps(taskType);

        return currentTps >= SpeedControlConstant.serverTps;
    }

    public static int currentTps(String taskType) {
        TaskStateSingleton taskStateSingleton = TaskStateSingleton.getInstance();
        TaskStateDO currentTask = taskStateSingleton.getTaskStateDO(taskType);
        if (currentTask == null) {
            // 这里有并发问题，但是统计tps不需要特别精准。添加了并发控制反而会影响性能
            currentTask = new TaskStateDO();
            taskStateSingleton.putTaskStateDO(taskType, currentTask);
        }
        return currentTask.calcuTps();
    }

    // 在map里设置时间
    public static void setExecTime(String taskType, long time) {
        TaskStateSingleton taskStateSingleton = TaskStateSingleton.getInstance();
        TaskStateDO currentTask = taskStateSingleton.getTaskStateDO(taskType);
        if (currentTask == null) {
            // logger..
            return;
        }
        currentTask.state(time);
    }
}
