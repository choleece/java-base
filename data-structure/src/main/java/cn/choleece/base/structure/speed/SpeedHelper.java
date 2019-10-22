package cn.choleece.base.structure.speed;

/**
 * @author choleece
 * @Description: TODO
 * @Date 2019-10-22 20:13
 **/
public class SpeedHelper {

    public static boolean needSpeedControl(String task) {
        int currentTps = currentTps(task);

        return currentTps >= SpeedConstant.THRESHOLD_TPS;
    }

    public static int currentTps(String task) {
        TaskSingleton singleton = TaskSingleton.singleton;

        SpeedTaskState taskState = singleton.getTask(task);
        if (taskState == null) {
            taskState = new SpeedTaskState(task);

            singleton.putTask(task, taskState);
        }

        return taskState.calculateTaskTps();
    }

    public static void setTask(String task, long exeTime) {
        TaskSingleton singleton = TaskSingleton.singleton;

        SpeedTaskState taskState = singleton.getTask(task);
        if (taskState == null) {
            // 直接return 即可，正常情况下不会出现这种情况，因为上边的currentTps会在setTask之前执行
            return;
        }

        taskState.setTime(exeTime);
    }
}
